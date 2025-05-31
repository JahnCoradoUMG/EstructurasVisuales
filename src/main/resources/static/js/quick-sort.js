class QuickSortVisualizer {
  constructor() {
    this.steps = []
    this.currentStep = 0
    this.isPlaying = false
    this.playInterval = null

    this.initializeElements()
    this.bindEvents()
  }

  initializeElements() {
    this.arrayInput = document.getElementById("array-input")
    this.sortButton = document.getElementById("sort-button")
    this.arrayContainer = document.getElementById("array-container")
    this.description = document.getElementById("description")
    this.prevButton = document.getElementById("prev-step")
    this.nextButton = document.getElementById("next-step")
    this.playPauseButton = document.getElementById("play-pause")
    this.controlsContainer = document.getElementById("controls")

    // Ocultar controles inicialmente
    this.controlsContainer.style.display = "none"
  }

  bindEvents() {
    this.sortButton.addEventListener("click", () => this.startSort())
    this.prevButton.addEventListener("click", () => this.previousStep())
    this.nextButton.addEventListener("click", () => this.nextStep())
    this.playPauseButton.addEventListener("click", () => this.togglePlayPause())
  }

  async startSort() {
    const arrayText = this.arrayInput.value.trim()
    if (!arrayText) {
      alert("Por favor ingrese números separados por comas")
      return
    }

    try {
      const array = arrayText.split(",").map((num) => Number.parseInt(num.trim()))
      if (array.some(isNaN)) {
        alert("Por favor ingrese solo números válidos")
        return
      }

      this.sortButton.disabled = true
      this.sortButton.textContent = "Ordenando..."

      const response = await fetch("/algorithms/quick-sort/sort", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ array: array }),
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
      alert("Error al procesar el array: " + error.message)
    } finally {
      this.sortButton.disabled = false
      this.sortButton.textContent = "Ordenar"
    }
  }

  showStep(stepIndex) {
    if (stepIndex < 0 || stepIndex >= this.steps.length) return

    const step = this.steps[stepIndex]
    this.renderArray(step.data, step.highlightIndices || [])
    this.description.textContent = step.description

    // Actualizar estado de botones
    this.prevButton.disabled = stepIndex === 0
    this.nextButton.disabled = stepIndex === this.steps.length - 1

    // Actualizar texto del botón play/pause
    if (stepIndex === this.steps.length - 1) {
      this.playPauseButton.textContent = "Reiniciar"
      this.isPlaying = false
      if (this.playInterval) {
        clearInterval(this.playInterval)
        this.playInterval = null
      }
    }
  }

  renderArray(array, highlightIndices = []) {
    this.arrayContainer.innerHTML = ""

    array.forEach((value, index) => {
      const element = document.createElement("div")
      element.className = "array-element"
      element.textContent = value

      if (highlightIndices.includes(index)) {
        element.classList.add("highlight")
      }

      this.arrayContainer.appendChild(element)
    })
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
      // Reiniciar
      this.currentStep = 0
      this.showStep(this.currentStep)
      this.playPauseButton.textContent = "Reproducir"
      return
    }

    if (this.isPlaying) {
      // Pausar
      this.isPlaying = false
      this.playPauseButton.textContent = "Reproducir"
      if (this.playInterval) {
        clearInterval(this.playInterval)
        this.playInterval = null
      }
    } else {
      // Reproducir
      this.isPlaying = true
      this.playPauseButton.textContent = "Pausar"
      this.playInterval = setInterval(() => {
        if (this.currentStep < this.steps.length - 1) {
          this.currentStep++
          this.showStep(this.currentStep)
        } else {
          this.togglePlayPause() // Auto-pausar al final
        }
      }, 800) // Más rápido para Quick Sort
    }
  }
}

// Inicializar cuando se carga la página
document.addEventListener("DOMContentLoaded", () => {
  new QuickSortVisualizer()
})
