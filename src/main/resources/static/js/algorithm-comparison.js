import { Chart } from "@/components/ui/chart"
class AlgorithmComparisonVisualizer {
  constructor() {
    this.comparison = null
    this.currentStep = 0
    this.isPlaying = false
    this.playInterval = null
    this.chart = null

    this.initializeElements()
    this.bindEvents()
  }

  initializeElements() {
    this.arrayInput = document.getElementById("array-input")
    this.algorithm1Select = document.getElementById("algorithm1-select")
    this.algorithm2Select = document.getElementById("algorithm2-select")
    this.compareButton = document.getElementById("compare-button")
    this.comparisonContainer = document.getElementById("comparison-container")
    this.algorithm1Title = document.getElementById("algorithm1-title")
    this.algorithm2Title = document.getElementById("algorithm2-title")
    this.array1Container = document.getElementById("array1-container")
    this.array2Container = document.getElementById("array2-container")
    this.metrics1 = document.getElementById("metrics1")
    this.metrics2 = document.getElementById("metrics2")
    this.description = document.getElementById("description")
    this.prevButton = document.getElementById("prev-step")
    this.nextButton = document.getElementById("next-step")
    this.playPauseButton = document.getElementById("play-pause")
    this.resetButton = document.getElementById("reset-button")
    this.controlsContainer = document.getElementById("controls")
    this.chartContainer = document.getElementById("performance-chart-container")

    // Ocultar controles inicialmente
    this.controlsContainer.style.display = "none"
    this.chartContainer.style.display = "none"

    console.log("Elementos inicializados correctamente")
  }

  bindEvents() {
    this.compareButton.addEventListener("click", () => {
      console.log("Botón comparar clickeado")
      this.startComparison()
    })
    this.prevButton.addEventListener("click", () => this.previousStep())
    this.nextButton.addEventListener("click", () => this.nextStep())
    this.playPauseButton.addEventListener("click", () => this.togglePlayPause())
    this.resetButton.addEventListener("click", () => this.resetComparison())

    console.log("Event listeners configurados")
  }

  async startComparison() {
    console.log("=== INICIANDO COMPARACIÓN ===")

    const arrayText = this.arrayInput.value.trim()
    console.log("Array input:", arrayText)

    if (!arrayText) {
      alert("Por favor ingrese números separados por comas")
      return
    }

    const algorithm1 = this.algorithm1Select.value
    const algorithm2 = this.algorithm2Select.value

    console.log("Algoritmo 1 seleccionado:", algorithm1)
    console.log("Algoritmo 2 seleccionado:", algorithm2)

    if (algorithm1 === algorithm2) {
      alert("Por favor seleccione dos algoritmos diferentes")
      return
    }

    try {
      const array = arrayText.split(",").map((num) => Number.parseInt(num.trim()))
      console.log("Array parseado:", array)

      if (array.some(isNaN)) {
        alert("Por favor ingrese solo números válidos")
        return
      }

      this.compareButton.disabled = true
      this.compareButton.textContent = "Comparando..."

      const requestBody = {
        array: array,
        algorithm1: algorithm1,
        algorithm2: algorithm2,
      }

      console.log("Enviando request:", JSON.stringify(requestBody))

      const response = await fetch("/algorithms/comparison/compare", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(requestBody),
      })

      console.log("Response status:", response.status)
      console.log("Response headers:", response.headers)

      if (!response.ok) {
        const errorText = await response.text()
        console.error("Error response:", errorText)
        throw new Error(`Error ${response.status}: ${errorText}`)
      }

      const responseText = await response.text()
      console.log("Response text:", responseText)

      this.comparison = JSON.parse(responseText)
      console.log("Comparison result:", this.comparison)

      this.currentStep = 0
      this.setupComparison()
      this.showStep(this.currentStep)
      this.controlsContainer.style.display = "flex"
      this.chartContainer.style.display = "block"
      this.createPerformanceChart()

      console.log("=== COMPARACIÓN COMPLETADA ===")
    } catch (error) {
      console.error("Error completo:", error)
      alert("Error al comparar algoritmos: " + error.message)
    } finally {
      this.compareButton.disabled = false
      this.compareButton.textContent = "Comparar Algoritmos"
    }
  }

  setupComparison() {
    console.log("Configurando comparación...")
    this.algorithm1Title.textContent = this.comparison.algorithm1.name
    this.algorithm2Title.textContent = this.comparison.algorithm2.name

    // Mostrar métricas finales
    this.updateMetrics(this.metrics1, this.comparison.algorithm1.metrics)
    this.updateMetrics(this.metrics2, this.comparison.algorithm2.metrics)

    console.log("Comparación configurada")
  }

  updateMetrics(container, metrics) {
    container.innerHTML = `
      <div class="metric-item">
        <span class="metric-label">Comparaciones:</span>
        <span class="metric-value">${metrics.comparisons}</span>
      </div>
      <div class="metric-item">
        <span class="metric-label">Intercambios:</span>
        <span class="metric-value">${metrics.swaps}</span>
      </div>
      <div class="metric-item">
        <span class="metric-label">Pasos totales:</span>
        <span class="metric-value">${metrics.totalSteps}</span>
      </div>
      <div class="metric-item">
        <span class="metric-label">Tiempo:</span>
        <span class="metric-value">${metrics.executionTimeMs}ms</span>
      </div>
    `
  }

  showStep(stepIndex) {
    const maxSteps = Math.max(this.comparison.algorithm1.steps.length, this.comparison.algorithm2.steps.length)

    if (stepIndex < 0 || stepIndex >= maxSteps) return

    // Mostrar paso del algoritmo 1
    if (stepIndex < this.comparison.algorithm1.steps.length) {
      const step1 = this.comparison.algorithm1.steps[stepIndex]
      this.renderArray(this.array1Container, step1.data, step1.highlightIndices || [])
    }

    // Mostrar paso del algoritmo 2
    if (stepIndex < this.comparison.algorithm2.steps.length) {
      const step2 = this.comparison.algorithm2.steps[stepIndex]
      this.renderArray(this.array2Container, step2.data, step2.highlightIndices || [])
    }

    // Mostrar descripción del paso actual
    let description = `Paso ${stepIndex + 1} de ${maxSteps}\n\n`
    if (stepIndex < this.comparison.algorithm1.steps.length) {
      description += `${this.comparison.algorithm1.name}: ${this.comparison.algorithm1.steps[stepIndex].description}\n\n`
    }
    if (stepIndex < this.comparison.algorithm2.steps.length) {
      description += `${this.comparison.algorithm2.name}: ${this.comparison.algorithm2.steps[stepIndex].description}`
    }

    this.description.style.whiteSpace = "pre-line"
    this.description.textContent = description

    // Actualizar estado de botones
    this.prevButton.disabled = stepIndex === 0
    this.nextButton.disabled = stepIndex === maxSteps - 1

    // Actualizar texto del botón play/pause
    if (stepIndex === maxSteps - 1) {
      this.playPauseButton.textContent = "Reiniciar"
      this.isPlaying = false
      if (this.playInterval) {
        clearInterval(this.playInterval)
        this.playInterval = null
      }
    }
  }

  renderArray(container, array, highlightIndices = []) {
    container.innerHTML = ""

    array.forEach((value, index) => {
      const element = document.createElement("div")
      element.className = "array-element"
      element.textContent = value

      if (highlightIndices.includes(index)) {
        element.classList.add("highlight")
      }

      container.appendChild(element)
    })
  }

  createPerformanceChart() {
    const ctx = document.getElementById("performance-chart")
    if (!ctx) {
      console.error("No se encontró el canvas para el gráfico")
      return
    }

    if (this.chart) {
      this.chart.destroy()
    }

    const data = {
      labels: ["Comparaciones", "Intercambios", "Pasos Totales"],
      datasets: [
        {
          label: this.comparison.algorithm1.name,
          data: [
            this.comparison.algorithm1.metrics.comparisons,
            this.comparison.algorithm1.metrics.swaps,
            this.comparison.algorithm1.metrics.totalSteps,
          ],
          backgroundColor: "rgba(102, 126, 234, 0.6)",
          borderColor: "rgba(102, 126, 234, 1)",
          borderWidth: 2,
        },
        {
          label: this.comparison.algorithm2.name,
          data: [
            this.comparison.algorithm2.metrics.comparisons,
            this.comparison.algorithm2.metrics.swaps,
            this.comparison.algorithm2.metrics.totalSteps,
          ],
          backgroundColor: "rgba(255, 107, 107, 0.6)",
          borderColor: "rgba(255, 107, 107, 1)",
          borderWidth: 2,
        },
      ],
    }

    try {
      this.chart = new Chart(ctx, {
        type: "bar",
        data: data,
        options: {
          responsive: true,
          plugins: {
            title: {
              display: true,
              text: "Comparación de Rendimiento",
            },
            legend: {
              display: true,
              position: "top",
            },
          },
          scales: {
            y: {
              beginAtZero: true,
              title: {
                display: true,
                text: "Cantidad",
              },
            },
          },
        },
      })
      console.log("Gráfico creado exitosamente")
    } catch (error) {
      console.error("Error creando el gráfico:", error)
    }
  }

  previousStep() {
    if (this.currentStep > 0) {
      this.currentStep--
      this.showStep(this.currentStep)
    }
  }

  nextStep() {
    const maxSteps = Math.max(this.comparison.algorithm1.steps.length, this.comparison.algorithm2.steps.length)

    if (this.currentStep < maxSteps - 1) {
      this.currentStep++
      this.showStep(this.currentStep)
    }
  }

  togglePlayPause() {
    const maxSteps = Math.max(this.comparison.algorithm1.steps.length, this.comparison.algorithm2.steps.length)

    if (this.currentStep === maxSteps - 1) {
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
        if (this.currentStep < maxSteps - 1) {
          this.currentStep++
          this.showStep(this.currentStep)
        } else {
          this.togglePlayPause() // Auto-pausar al final
        }
      }, 1200) // Velocidad moderada para comparación
    }
  }

  resetComparison() {
    this.currentStep = 0
    if (this.comparison) {
      this.showStep(this.currentStep)
    }
    this.isPlaying = false
    this.playPauseButton.textContent = "Reproducir"
    if (this.playInterval) {
      clearInterval(this.playInterval)
      this.playInterval = null
    }
  }
}

// Inicializar cuando se carga la página
document.addEventListener("DOMContentLoaded", () => {
  console.log("DOM cargado, inicializando AlgorithmComparisonVisualizer...")
  new AlgorithmComparisonVisualizer()
})
