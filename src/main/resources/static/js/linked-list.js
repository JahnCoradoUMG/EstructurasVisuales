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
    this.removeFirstButton = document.getElementById("remove-first-button")
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
    this.removeFirstButton.addEventListener("click", () => this.removeFirst())
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
  }

  async addLast() {
    const value = Number.parseInt(this.valueInput.value)
    if (isNaN(value)) {
      alert("Por favor ingrese un número válido")
      return
    }

    await this.performOperation("/data-structures/linked-list/add-last", { value })
  }

  async removeFirst() {
    await this.performOperation("/data-structures/linked-list/remove-first", {})
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

    nodes.forEach((node, index) => {
      const nodeElement = this.createNodeElement(node, index, highlightIndices.includes(index))
      this.listContainer.appendChild(nodeElement)
    })
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

    // Agregar flechas si hay conexiones
    if (node.next !== null) {
      const arrowNext = document.createElement("div")
      arrowNext.className = "arrow arrow-next"
      nodeDiv.appendChild(arrowNext)
    }

    if (node.prev !== null) {
      const arrowPrev = document.createElement("div")
      arrowPrev.className = "arrow arrow-prev"
      nodeDiv.appendChild(arrowPrev)
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
