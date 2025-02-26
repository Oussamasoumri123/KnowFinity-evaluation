import { Component, AfterViewInit } from '@angular/core';
import Chart from 'chart.js/auto'; // ✅ Import Chart.js properly

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements AfterViewInit {

  ngAfterViewInit(): void {
    this.loadScript('assets/BackOffice/js/chart.min.js');
    this.loadScript('assets/BackOffice/js/bs-init.js');
    this.loadScript('assets/BackOffice/js/theme.js');

    setTimeout(() => {
      this.initCharts();
    }, 500); // Delay to ensure DOM is ready
  }

  loadScript(src: string): void {
    const script = document.createElement('script');
    script.src = src;
    script.async = true;
    document.body.appendChild(script);
  }

  initCharts(): void {
    console.log("Initializing Charts...");

    const charts = Array.from(document.querySelectorAll('[data-bss-chart]')) as HTMLCanvasElement[];

    charts.forEach(chart => {
      const chartCanvas = chart as HTMLCanvasElement & { customChart?: Chart }; // ✅ Fix TypeScript error

      if (!chartCanvas.customChart) { // ✅ Prevent multiple initializations
        chartCanvas.customChart = new Chart(chartCanvas, JSON.parse(chartCanvas.getAttribute('data-bss-chart')!));
      }
    });
  }
}
