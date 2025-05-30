// Import D3 library
const d3 = window.d3

class BinaryTreeVisualizer {
  constructor() {
    this.steps = []
    this.currentStep = 0
    this.isPlaying = false
    this.playInterval = null
    this.svg = null

    this.initializeElements()
    this.bindEvents()
    this.initializeSVG()
    this.loadInitialData()
  }

  initializeElements() {
    this.valueInput = document.getElementById("value-input")
    this.insertButton = document.getElementById("insert-button")
    this.treeContainer = document.getElementById("tree-container")
    this.description = document.getElementById("description")
    this.prevButton = document.getElementById("prev-step")
    this.nextButton = document.getElementById("next-step")
    this.playPauseButton = document.getElementById("play-pause")
    this.controlsContainer = document.getElementById("controls")

    this.controlsContainer.style.display = "none"
  }

  bindEvents() {
    this.insertButton.addEventListener("click", () => this.insert())
    this.prevButton.addEventListener("click", () => this.previousStep())
    this.nextButton.addEventListener("click", () => this.nextStep())
    this.playPauseButton.addEventListener("click", () => this.togglePlayPause())
  }

  initializeSVG() {
    this.svg = d3.select("#tree-container").append("svg").attr("width", 800).attr("height", 400)
  }

  async loadInitialData() {
    try {
      const response = await fetch("/data-structures/binary-tree/data")
      const data = await response.json()
      this.renderTree(data.first, data.second)
    } catch (error) {
      console.error("Error loading initial data:", error)
    }
  }

  async insert() {
    const value = Number.parseInt(this.valueInput.value)
    if (isNaN(value)) {
      alert("Por favor ingrese un número válido")
      return
    }

    try {
      const response = await fetch("/data-structures/binary-tree/insert", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ value }),
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
      alert("Error al insertar: " + error.message)
    }
  }

  showStep(stepIndex) {
    if (stepIndex < 0 || stepIndex >= this.steps.length) return

    const step = this.steps[stepIndex]
    this.renderTreeFromStep(step.data, step.highlightIndices || [])
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

  renderTreeFromStep(nodes, highlightIndices = []) {
    if (nodes.length === 0) {
      this.svg.selectAll("*").remove()
      this.svg.append("text").attr("x", 400).attr("y", 200).attr("text-anchor", "middle").text("Árbol vacío")
      return
    }

    const treeData = this.buildTreeStructure(nodes, 0)
    this.renderD3Tree(treeData, highlightIndices)
  }

  renderTree(nodes, rootIndex) {
    if (nodes.length === 0 || rootIndex === null) {
      this.svg.selectAll("*").remove()
      this.svg.append("text").attr("x", 400).attr("y", 200).attr("text-anchor", "middle").text("Árbol vacío")
      return
    }

    const treeData = this.buildTreeStructure(nodes, rootIndex)
    this.renderD3Tree(treeData, [])
  }

  buildTreeStructure(nodes, rootIndex) {
    if (rootIndex === null || rootIndex >= nodes.length) return null

    const node = nodes[rootIndex]
    const treeNode = {
      name: node.value.toString(),
      index: rootIndex,
      children: [],
    }

    if (node.leftIndex !== null) {
      const leftChild = this.buildTreeStructure(nodes, node.leftIndex)
      if (leftChild) treeNode.children.push(leftChild)
    }

    if (node.rightIndex !== null) {
      const rightChild = this.buildTreeStructure(nodes, node.rightIndex)
      if (rightChild) treeNode.children.push(rightChild)
    }

    return treeNode
  }

  renderD3Tree(treeData, highlightIndices = []) {
    this.svg.selectAll("*").remove()

    if (!treeData) return

    const width = 800
    const height = 400

    const tree = d3.tree().size([width - 100, height - 100])
    const root = d3.hierarchy(treeData)

    tree(root)

    // Dibujar enlaces
    this.svg
      .selectAll(".tree-link")
      .data(root.links())
      .enter()
      .append("path")
      .attr("class", "tree-link")
      .attr(
        "d",
        d3
          .linkVertical()
          .x((d) => d.x + 50)
          .y((d) => d.y + 50),
      )

    // Dibujar nodos
    const nodes = this.svg
      .selectAll(".tree-node")
      .data(root.descendants())
      .enter()
      .append("g")
      .attr("transform", (d) => `translate(${d.x + 50}, ${d.y + 50})`)

    nodes
      .append("circle")
      .attr("class", (d) => {
        let className = "tree-node"
        if (highlightIndices.includes(d.data.index)) {
          className += " highlight"
        }
        return className
      })
      .attr("r", 25)
      .attr("fill", (d) => (highlightIndices.includes(d.data.index) ? "#ff6b6b" : "#667eea"))
      .attr("stroke", (d) => (highlightIndices.includes(d.data.index) ? "#e74c3c" : "#2980b9"))
      .attr("stroke-width", 3)

    nodes
      .append("text")
      .attr("class", "tree-label")
      .attr("dy", "0.35em")
      .attr("fill", "white")
      .attr("font-size", "16px")
      .attr("font-weight", "700")
      .attr("text-anchor", "middle")
      .style("text-shadow", "1px 1px 2px rgba(0,0,0,0.5)")
      .text((d) => d.data.name)
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
      }, 1500)
    }
  }
}

document.addEventListener("DOMContentLoaded", () => {
  new BinaryTreeVisualizer()
})
