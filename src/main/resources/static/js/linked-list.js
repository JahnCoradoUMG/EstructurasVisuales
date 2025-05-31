class LinkedListVisualizer {
  constructor() {
    this.steps = []
    this.currentStep = 0
    this.isPlaying = false
    this.playInterval = null

    this.initializeElements()
    this.bindEvents()
    this.loadInitialData()
  }

  initializeElements() {
    this.valueInput = document.getElementById("value-input")
    this.addFirstButton = document.getElementById("add-first-button")
    this.addLastButton = document.getElementById("add-last-button")
    this.searchButton = document.getElementById("search-button")
    this.removeFirstButton = document.getElementById("remove-first-button")
    this.removeByValueButton = document.getElementById("remove-by-value-button")
    this.removeNodesButton = document.getElementById("remove-nodes-button")
    this.listContainer = document.getElementById("linked-list-container")
    this.description = document.getElementById("description")
    this.prevButton = document.getElementById("prev-step")
    this.nextButton = document.getElementById("next-step")
    this.playPauseButton = document.getElementById("play-pause")
    this.controlsContainer = document.getElementById("controls")

    this.controlsContainer.style.display = "none"
  }

  bindEvents() {
    this.addFirstButton.addEventListener("click", () => this.addFirst())
    this.addLastButton.addEventListener("click", () => this.addLast())
    this.searchButton.addEventListener("click", () => this.search())
    this.removeFirstButton.addEventListener("click", () => this.removeFirst())
    this.removeByValueButton.addEventListener("click", () => this.removeByValue())
    this.removeNodesButton.addEventListener("click", () => this.removeNodes())
    this.prevButton.addEventListener("click", () => this.previousStep())
    this.nextButton.addEventListener("click", () => this.nextStep())
    this.playPauseButton.addEventListener("click", () => this.togglePlayPause())
  }

  async loadInitialData() {
    try {
      const response = await fetch("/data-structures/linked-list/data")
      const data = await response.json()
      this.renderList(data.first, data.second.first, data.second.second)
    } catch (error) {
      console.error("Error loading initial data:", error)
    }
  }

  async addFirst() {
    const value = Number.parseInt(this.valueInput.value)
    if (isNaN(value)) {
      alert("Por favor ingrese un número válido")
      return
    }

    await this.performOperation("/data-structures/linked-list/add-first", { value })
    this.nextStep()
  }

  async addLast() {
    const value = Number.parseInt(this.valueInput.value)
    if (isNaN(value)) {
      alert("Por favor ingrese un número válido")
      return
    }

    await this.performOperation("/data-structures/linked-list/add-last", { value })
    this.nextStep()
  }

  async removeFirst() {
    await this.performOperation("/data-structures/linked-list/remove-first", {})
    this.nextStep()
  }

  async removeNodes() {
    await this.performOperation("/data-structures/linked-list/remove-nodes", {})
    this.nextStep()
  }

  async search() {
    const value = Number.parseInt(this.valueInput.value)
    if (isNaN(value)) {
      alert("Por favor ingrese un número válido")
      return
    }

    await this.performOperation("/data-structures/linked-list/search", { value })
  }

  async removeByValue() {
    const value = Number.parseInt(this.valueInput.value)
    if (isNaN(value)) {
      alert("Por favor ingrese un número válido")
      return
    }

    await this.performOperation("/data-structures/linked-list/remove-by-value", { value })
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
    this.renderListFromStep(step.data, step.highlightIndices || [])
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

  renderListFromStep(nodes, highlightIndices = []) {
    this.listContainer.innerHTML = ""

    if (nodes.length === 0) {
      this.listContainer.innerHTML = '<div class="empty-list">Lista vacía</div>'
      return
    }

    // Agregar información de la lista
    const listInfo = document.createElement("div")
    listInfo.className = "list-info"
    listInfo.innerHTML = `
    <strong>Información de la Lista:</strong><br>
    Tamaño: ${nodes.length} nodos
  `
    this.listContainer.appendChild(listInfo)

    const nodesContainer = document.createElement("div")
    nodesContainer.style.display = "flex"
    nodesContainer.style.justifyContent = "center"
    nodesContainer.style.alignItems = "center"
    nodesContainer.style.flexWrap = "wrap"
    nodesContainer.style.gap = "20px"
    nodesContainer.style.marginTop = "1rem"

    nodes.forEach((node, index) => {
      const nodeElement = this.createNodeElement(node, index, highlightIndices.includes(index))
      nodesContainer.appendChild(nodeElement)
    })

    this.listContainer.appendChild(nodesContainer)
  }

  renderList(nodes, headIndex, tailIndex) {
    this.listContainer.innerHTML = ""

    if (nodes.length === 0) {
      this.listContainer.innerHTML = '<div class="empty-list">Lista vacía</div>'
      return
    }

    nodes.forEach((node, index) => {
      const nodeElement = this.createNodeElement(node, index, false, headIndex === index, tailIndex === index)
      this.listContainer.appendChild(nodeElement)
    })
  }

  createNodeElement(node, index, highlight = false, isHead = false, isTail = false) {
    const nodeDiv = document.createElement("div")
    nodeDiv.className = "node"

    const nodeContent = document.createElement("div")
    nodeContent.className = "node-content"
    nodeContent.textContent = node.value

    // Agregar información del índice
    const indexLabel = document.createElement("div")
    indexLabel.style.position = "absolute"
    indexLabel.style.top = "-25px"
    indexLabel.style.left = "50%"
    indexLabel.style.transform = "translateX(-50%)"
    indexLabel.style.fontSize = "12px"
    indexLabel.style.fontWeight = "600"
    indexLabel.style.color = "#667eea"
    indexLabel.style.background = "white"
    indexLabel.style.padding = "2px 6px"
    indexLabel.style.borderRadius = "6px"
    indexLabel.style.boxShadow = "0 2px 4px rgba(0,0,0,0.1)"
    indexLabel.textContent = `[${index}]`
    nodeContent.appendChild(indexLabel)

    if (highlight) {
      nodeContent.classList.add("highlight")
    }
    if (isHead) {
      nodeContent.classList.add("head")
    }
    if (isTail) {
      nodeContent.classList.add("tail")
    }

    nodeDiv.appendChild(nodeContent)

    // Agregar conexiones bidireccionales si hay siguiente nodo
    if (node.next !== null) {
      const connectionsDiv = document.createElement("div")
      connectionsDiv.className = "node-connections"

      const connectionLine = document.createElement("div")
      connectionLine.className = "connection-line"

      const arrowRight = document.createElement("div")
      arrowRight.className = "arrow-right"

      const arrowLeft = document.createElement("div")
      arrowLeft.className = "arrow-left"

      connectionsDiv.appendChild(connectionLine)
      connectionsDiv.appendChild(arrowRight)
      connectionsDiv.appendChild(arrowLeft)

      nodeDiv.appendChild(connectionsDiv)
    }

    return nodeDiv
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
  new LinkedListVisualizer()
})
