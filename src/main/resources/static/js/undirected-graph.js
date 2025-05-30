// Import d3 library
const d3 = window.d3

class UndirectedGraphVisualizer {
  constructor() {
    this.steps = []
    this.currentStep = 0
    this.isPlaying = false
    this.playInterval = null
    this.svg = null
    this.simulation = null

    this.initializeElements()
    this.bindEvents()
    this.initializeSVG()
    this.loadInitialData()
  }

  initializeElements() {
    this.nodeValueInput = document.getElementById("node-value-input")
    this.addNodeButton = document.getElementById("add-node-button")
    this.fromIdInput = document.getElementById("from-id-input")
    this.toIdInput = document.getElementById("to-id-input")
    this.weightInput = document.getElementById("weight-input")
    this.addEdgeButton = document.getElementById("add-edge-button")
    this.graphContainer = document.getElementById("graph-container")
    this.description = document.getElementById("description")
    this.prevButton = document.getElementById("prev-step")
    this.nextButton = document.getElementById("next-step")
    this.playPauseButton = document.getElementById("play-pause")
    this.controlsContainer = document.getElementById("controls")

    this.controlsContainer.style.display = "none"
  }

  bindEvents() {
    this.addNodeButton.addEventListener("click", () => this.addNode())
    this.addEdgeButton.addEventListener("click", () => this.addEdge())
    this.prevButton.addEventListener("click", () => this.previousStep())
    this.nextButton.addEventListener("click", () => this.nextStep())
    this.playPauseButton.addEventListener("click", () => this.togglePlayPause())
  }

  initializeSVG() {
    this.svg = d3.select("#graph-container").append("svg").attr("width", 800).attr("height", 400)
  }

  async loadInitialData() {
    try {
      const response = await fetch("/data-structures/undirected-graph/data")
      const data = await response.json()
      this.renderGraph(data)
    } catch (error) {
      console.error("Error loading initial data:", error)
    }
  }

  async addNode() {
    const value = this.nodeValueInput.value.trim()
    if (!value) {
      alert("Por favor ingrese un valor para el nodo")
      return
    }

    await this.performOperation("/data-structures/undirected-graph/add-node", { value })
  }

  async addEdge() {
    const fromId = Number.parseInt(this.fromIdInput.value)
    const toId = Number.parseInt(this.toIdInput.value)
    const weight = Number.parseFloat(this.weightInput.value)

    if (isNaN(fromId) || isNaN(toId) || isNaN(weight)) {
      alert("Por favor ingrese valores válidos para la arista")
      return
    }

    await this.performOperation("/data-structures/undirected-graph/add-edge", { fromId, toId, weight })
  }

  async performOperation(endpoint, data) {
    try {
      const response = await fetch(endpoint, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
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
      alert("Error al realizar la operación: " + error.message)
    }
  }

  showStep(stepIndex) {
    if (stepIndex < 0 || stepIndex >= this.steps.length) return

    const step = this.steps[stepIndex]
    this.renderGraph(step.data)
    this.description.textContent = step.description

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

  renderGraph(graphData) {
    this.svg.selectAll("g").remove()

    if (graphData.nodes.length === 0) {
      this.svg.append("text").attr("x", 400).attr("y", 200).attr("text-anchor", "middle").text("Grafo vacío")
      return
    }

    const nodes = graphData.nodes.map((d) => ({ ...d }))
    // Para grafo no dirigido, filtramos aristas duplicadas
    const uniqueEdges = this.getUniqueEdges(graphData.edges)
    const links = uniqueEdges.map((d) => ({ ...d }))

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

    const link = linkGroup
      .selectAll("line")
      .data(links)
      .enter()
      .append("line")
      .attr("stroke", "#2c3e50")
      .attr("stroke-width", 2)

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

    node.append("circle").attr("r", 20).attr("fill", "#3498db").attr("stroke", "#2980b9").attr("stroke-width", 2)

    node
      .append("text")
      .attr("text-anchor", "middle")
      .attr("dy", "0.35em")
      .attr("fill", "white")
      .text((d) => d.value)

    // Etiquetas de peso en las aristas
    const edgeLabels = linkGroup
      .selectAll("text")
      .data(links)
      .enter()
      .append("text")
      .attr("text-anchor", "middle")
      .attr("font-size", "12px")
      .attr("fill", "#2c3e50")
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

  getUniqueEdges(edges) {
    const seen = new Set()
    return edges.filter((edge) => {
      const key = [Math.min(edge.from, edge.to), Math.max(edge.from, edge.to)].join("-")
      if (seen.has(key)) {
        return false
      }
      seen.add(key)
      return true
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
  new UndirectedGraphVisualizer()
})
