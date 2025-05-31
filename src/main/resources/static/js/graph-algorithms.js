// Import d3 library
const d3 = window.d3

class GraphAlgorithmVisualizer {
  constructor(algorithmType) {
    this.algorithmType = algorithmType
    this.steps = []
    this.currentStep = 0
    this.isPlaying = false
    this.playInterval = null
    this.svg = null
    this.simulation = null

    this.initializeElements()
    this.bindEvents()
    this.initializeSVG()
    this.loadSampleGraph()
  }

  initializeElements() {
    this.startNodeInput = document.getElementById("start-node-input")
    this.endNodeInput = document.getElementById("end-node-input")
    this.executeButton = document.getElementById("execute-button")
    this.graphContainer = document.getElementById("graph-container")
    this.algorithmInfo = document.getElementById("algorithm-info")
    this.description = document.getElementById("description")
    this.prevButton = document.getElementById("prev-step")
    this.nextButton = document.getElementById("next-step")
    this.playPauseButton = document.getElementById("play-pause")
    this.controlsContainer = document.getElementById("controls")

    this.controlsContainer.style.display = "none"
  }

  bindEvents() {
    this.executeButton.addEventListener("click", () => this.executeAlgorithm())
    this.prevButton.addEventListener("click", () => this.previousStep())
    this.nextButton.addEventListener("click", () => this.nextStep())
    this.playPauseButton.addEventListener("click", () => this.togglePlayPause())
  }

  initializeSVG() {
    this.svg = d3.select("#graph-container").append("svg").attr("width", 800).attr("height", 400)

    // Definir marcadores para las flechas
    this.svg
      .append("defs")
      .append("marker")
      .attr("id", "arrowhead")
      .attr("viewBox", "-0 -5 10 10")
      .attr("refX", 25)
      .attr("refY", 0)
      .attr("orient", "auto")
      .attr("markerWidth", 8)
      .attr("markerHeight", 8)
      .attr("xoverflow", "visible")
      .append("svg:path")
      .attr("d", "M 0,-5 L 10 ,0 L 0,5")
      .attr("fill", "#2c3e50")
      .style("stroke", "none")
  }

  async loadSampleGraph() {
    try {
      const response = await fetch("/graph-algorithms/sample-graph")
      const data = await response.json()
      this.renderGraph(data)
    } catch (error) {
      console.error("Error loading sample graph:", error)
    }
  }

  async executeAlgorithm() {
    const startNodeId = Number.parseInt(this.startNodeInput.value)
    const endNodeIdValue = this.endNodeInput.value.trim()
    const endNodeId = endNodeIdValue ? Number.parseInt(endNodeIdValue) : null

    if (isNaN(startNodeId) || (endNodeIdValue && isNaN(endNodeId))) {
      alert("Por favor ingrese valores válidos para los nodos")
      return
    }

    try {
      const response = await fetch(`/graph-algorithms/${this.algorithmType}/execute`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ startNodeId, endNodeId }),
      })

      if (!response.ok) {
        throw new Error("Error en la respuesta del servidor")
      }

      this.steps = await response.json()
      this.currentStep = 0
      this.showStep(this.currentStep)
      this.controlsContainer.style.display = "flex"
    } catch (error) {
      console.error("Error:", error)
      alert("Error al ejecutar el algoritmo: " + error.message)
    }
  }

  showStep(stepIndex) {
    if (stepIndex < 0 || stepIndex >= this.steps.length) return

    const step = this.steps[stepIndex]
    this.renderGraphFromStep(step.data)
    this.description.textContent = step.description
    this.updateAlgorithmInfo(step.data)

    this.prevButton.disabled = stepIndex === 0
    this.nextButton.disabled = stepIndex === this.steps.length - 1

    if (stepIndex === this.steps.length - 1) {
      this.playPauseButton.textContent = "Reiniciar"
      this.isPlaying = false
      if (this.playInterval) {
        clearInterval(this.playInterval)
        this.playInterval = null
      }
    }
  }

  updateAlgorithmInfo(algorithmResult) {
    let infoHTML = ""

    if (algorithmResult.visitedNodes.length > 0) {
      infoHTML += `<div class="info-section"><strong>Nodos visitados:</strong> ${algorithmResult.visitedNodes.join(", ")}</div>`
    }

    if (algorithmResult.path.length > 0) {
      infoHTML += `<div class="info-section"><strong>Camino:</strong> ${algorithmResult.path.join(" → ")}</div>`
    }

    if (algorithmResult.queue && algorithmResult.queue.length > 0) {
      infoHTML += `<div class="info-section"><strong>Cola:</strong> [${algorithmResult.queue.join(", ")}]</div>`
    }

    if (algorithmResult.stack && algorithmResult.stack.length > 0) {
      infoHTML += `<div class="info-section"><strong>Pila:</strong> [${algorithmResult.stack.join(", ")}]</div>`
    }

    if (algorithmResult.distances && Object.keys(algorithmResult.distances).length > 0) {
      const distancesStr = Object.entries(algorithmResult.distances)
        .map(([node, dist]) => `${node}: ${dist >= 999999 ? "∞" : dist}`)
        .join(", ")
      infoHTML += `<div class="info-section"><strong>Distancias:</strong> {${distancesStr}}</div>`
    }

    this.algorithmInfo.innerHTML = infoHTML
  }

  renderGraphFromStep(algorithmResult) {
    this.renderGraph(
      algorithmResult.graph,
      algorithmResult.visitedNodes,
      algorithmResult.currentNode,
      algorithmResult.path,
      algorithmResult.distances,
    )
  }

  renderGraph(graphData, visitedNodes = [], currentNode = null, path = [], distances = {}) {
    this.svg.selectAll("g").remove()

    if (graphData.nodes.length === 0) {
      this.svg.append("text").attr("x", 400).attr("y", 200).attr("text-anchor", "middle").text("Grafo vacío")
      return
    }

    const nodes = graphData.nodes.map((d) => ({ ...d }))
    // Convertir edges para D3 - mapear from/to a source/target
    const links = graphData.edges.map((d) => ({
      ...d,
      source: d.from,
      target: d.to,
    }))

    // Detener simulación anterior si existe
    if (this.simulation) {
      this.simulation.stop()
    }

    this.simulation = d3
      .forceSimulation(nodes)
      .force(
        "link",
        d3
          .forceLink(links)
          .id((d) => d.id)
          .distance(100),
      )
      .force("charge", d3.forceManyBody().strength(-300))
      .force("center", d3.forceCenter(400, 200))

    const linkGroup = this.svg.append("g").attr("class", "links")
    const nodeGroup = this.svg.append("g").attr("class", "nodes")

    // Renderizar aristas
    const link = linkGroup
      .selectAll("line")
      .data(links)
      .enter()
      .append("line")
      .attr("stroke", (d) => {
        if (path.length > 1) {
          for (let i = 0; i < path.length - 1; i++) {
            if ((d.from === path[i] && d.to === path[i + 1]) || (d.to === path[i] && d.from === path[i + 1])) {
              return "#e74c3c"
            }
          }
        }
        return "#2c3e50"
      })
      .attr("stroke-width", (d) => {
        if (path.length > 1) {
          for (let i = 0; i < path.length - 1; i++) {
            if ((d.from === path[i] && d.to === path[i + 1]) || (d.to === path[i] && d.from === path[i + 1])) {
              return 4
            }
          }
        }
        return 2
      })
      .attr("marker-end", "url(#arrowhead)")

    // Renderizar nodos
    const node = nodeGroup
      .selectAll("g")
      .data(nodes)
      .enter()
      .append("g")
      .call(
        d3
          .drag()
          .on("start", this.dragstarted.bind(this))
          .on("drag", this.dragged.bind(this))
          .on("end", this.dragended.bind(this)),
      )

    node
      .append("circle")
      .attr("r", 25)
      .attr("fill", (d) => {
        if (d.id === currentNode) return "#e74c3c"
        if (visitedNodes.includes(d.id)) return "#27ae60"
        return "#3498db"
      })
      .attr("stroke", (d) => {
        if (d.id === currentNode) return "#c0392b"
        if (visitedNodes.includes(d.id)) return "#229954"
        return "#2980b9"
      })
      .attr("stroke-width", 3)

    // Etiquetas de nodos
    node
      .append("text")
      .attr("text-anchor", "middle")
      .attr("dy", "0.35em")
      .attr("fill", "white")
      .attr("font-weight", "bold")
      .text((d) => d.value)

    // Etiquetas de distancia para Dijkstra
    if (Object.keys(distances).length > 0) {
      node
        .append("text")
        .attr("text-anchor", "middle")
        .attr("dy", "-35px")
        .attr("fill", "#2c3e50")
        .attr("font-size", "12px")
        .attr("font-weight", "bold")
        .text((d) => {
          const dist = distances[d.id]
          return dist >= 999999 ? "∞" : dist.toString()
        })
    }

    // Etiquetas de peso en las aristas
    const edgeLabels = linkGroup
      .selectAll("text")
      .data(links)
      .enter()
      .append("text")
      .attr("text-anchor", "middle")
      .attr("font-size", "12px")
      .attr("fill", "#2c3e50")
      .attr("font-weight", "bold")
      .text((d) => d.weight)

    this.simulation.on("tick", () => {
      link
        .attr("x1", (d) => d.source.x)
        .attr("y1", (d) => d.source.y)
        .attr("x2", (d) => d.target.x)
        .attr("y2", (d) => d.target.y)

      node.attr("transform", (d) => `translate(${d.x}, ${d.y})`)

      edgeLabels.attr("x", (d) => (d.source.x + d.target.x) / 2).attr("y", (d) => (d.source.y + d.target.y) / 2)
    })
  }

  dragstarted(event, d) {
    if (!event.active) this.simulation.alphaTarget(0.3).restart()
    d.fx = d.x
    d.fy = d.y
  }

  dragged(event, d) {
    d.fx = event.x
    d.fy = event.y
  }

  dragended(event, d) {
    if (!event.active) this.simulation.alphaTarget(0)
    d.fx = null
    d.fy = null
  }

  previousStep() {
    if (this.currentStep > 0) {
      this.currentStep--
      this.showStep(this.currentStep)
    }
  }

  nextStep() {
    if (this.currentStep < this.steps.length - 1) {
      this.currentStep++
      this.showStep(this.currentStep)
    }
  }

  togglePlayPause() {
    if (this.currentStep === this.steps.length - 1) {
      this.currentStep = 0
      this.showStep(this.currentStep)
      this.playPauseButton.textContent = "Reproducir"
      return
    }

    if (this.isPlaying) {
      this.isPlaying = false
      this.playPauseButton.textContent = "Reproducir"
      if (this.playInterval) {
        clearInterval(this.playInterval)
        this.playInterval = null
      }
    } else {
      this.isPlaying = true
      this.playPauseButton.textContent = "Pausar"
      this.playInterval = setInterval(() => {
        if (this.currentStep < this.steps.length - 1) {
          this.currentStep++
          this.showStep(this.currentStep)
        } else {
          this.togglePlayPause()
        }
      }, 2000)
    }
  }
}

document.addEventListener("DOMContentLoaded", () => {
  const algorithmType = window.algorithmType || "dfs"
  new GraphAlgorithmVisualizer(algorithmType)
})
