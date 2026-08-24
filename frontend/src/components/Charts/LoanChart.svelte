<script>
    import {
        CategoryScale,
        Chart,
        Filler,
        Legend,
        LinearScale,
        LineController,
        LineElement,
        PointElement,
        Title,
        Tooltip,
    } from "chart.js";
    import { onMount } from "svelte";
    import { Line } from "svelte-chartjs";
    import { fetchLoanTrend } from "../../lib/api";
    import { filters } from "../../lib/stores";

    Chart.register(
        LineElement,
        PointElement,
        LineController,
        CategoryScale,
        LinearScale,
        Title,
        Tooltip,
        Legend,
        Filler,
    );

    let data = { labels: [], datasets: [] };
    let loading = true;

    async function load(evt) {
        loading = true;
        const rows = await fetchLoanTrend(evt.startDate, evt.endDate);
        data = {
            labels: rows.map((row) => `${row.monthName} ${row.year}`),
            datasets: [
                {
                    label: "Total Loans",
                    data: rows.map((row) => row.totalLoans),
                    borderColor: "#6366f1",
                    tension: 0.3,
                    fill: true,
                },
            ],
        };
        loading = false;
    }
    $: load($filters);

    const options = {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            legend: { position: "top" },
        },
        scales: {
            y: { beginAtZero: true },
        },
    };
</script>

{#if loading}
    <p>Loading...</p>
{:else}
    <Line {data} {options} />
{/if}
