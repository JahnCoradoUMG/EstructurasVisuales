class PriorityQueueVisualizer {
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
    this.priorityInput = document.getElementById("priority-input")
    this.enqueueButton = document.getElementById("enqueue-button")
    this.dequeueButton = document.getElementById("dequeue-button")
    this.queueContainer = document.getElementById("queue-container")
    this.description = document.getElementById("description")
    this.prevButton = document.getElementById("prev-step")
    this.nextButton = document.getElementById("next-step")
    this.playPauseButton = document.getElementById("play-pause")
    this.controlsContainer = document.getElementById("controls")

    this.controlsContainer.style.display = "none"
  }

  bindEvents() {
    this.enqueueButton.addEventListener("click", () => this.enqueue())
    this.dequeueButton.addEventListener("click", () => this.dequeue())
    this.prevButton.addEventListener("click", () => this.previousStep())
    this.nextButton.addEventListener("click", () => this.nextStep())
    this.playPauseButton.addEventListener("click", () => this.togglePlayPause())
  }

  async loadInitialData() {
    try {
      const response = await fetch("/data-structures/priority-queue/data")
      const data = await response.json()
      this.renderQueue(data)
    } catch (error) {
      console.error("Error loading initial data:", error)
    }
  }

  async enqueue() {
    const value = this.valueInput.value.trim()
    const priority = Number.parseInt(this.priorityInput.value)

    if (!value || isNaN(priority)) {
      alert("Por favor ingrese un valor y una prioridad válidos")
      return
    }

    await this.performOperation("/data-structures/priority-queue/enqueue", { value, priority })
  }

  async dequeue() {
    await this.performOperation("/data-structures/priority-queue/dequeue", {})
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
    this.renderQueueFromStep(step.data, step.highlightIndices || [])
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

  renderQueueFromStep(items, highlightIndices = []) {
    this.queueContainer.innerHTML = ""

    if (items.length === 0) {
      this.queueContainer.innerHTML = '<div class="empty-queue">Cola vacía</div>'
      return
    }

    items.forEach((item, index) => {
      const itemElement = this.createQueueItemElement(item, index, highlightIndices.includes(index))
      this.queueContainer.appendChild(itemElement)
    })
  }

  renderQueue(items) {
    this.queueContainer.innerHTML = ""

    if (items.length === 0) {
      this.queueContainer.innerHTML = '<div class="empty-queue">Cola vacía</div>'
      return
    }

    items.forEach((item, index) => {
      const itemElement = this.createQueueItemElement(item, index, false)
      this.queueContainer.appendChild(itemElement)
    })
  }

  createQueueItemElement(item, index, highlight = false) {
    const itemDiv = document.createElement("div")
    itemDiv.className = "queue-item"

    if (highlight) {
      itemDiv.classList.add("highlight")
    }

    const valueDiv = document.createElement("div")
    valueDiv.className = "queue-item-value"
    valueDiv.textContent = item.value

    const priorityDiv = document.createElement("div")
    priorityDiv.className = "queue-item-priority"
    priorityDiv.textContent = `Prioridad: ${item.priority}`

    itemDiv.appendChild(valueDiv)
    itemDiv.appendChild(priorityDiv)

    return itemDiv
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
  new PriorityQueueVisualizer()
})
