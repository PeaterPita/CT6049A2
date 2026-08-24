<script>
    import {
        BarController,
        BarElement,
        CategoryScale,
        Chart,
        Legend,
        LinearScale,
        Title,
        Tooltip,
    } from "chart.js";
    import { onMount } from "svelte";
    import { fetchOverdueSummary } from "../../lib/api";
    import { Bar } from "svelte-chartjs";

    Chart.register(
        BarElement,
        BarController,
        CategoryScale,
        LinearScale,
        Title,
        Tooltip,
        Legend,
    );

    let data = { labels: [], datasets: [] };
    let loading = true;

    onMount(async () => {
        const rows = await fetchOverdueSummary();
        data = {
            labels: rows.map((row) => row.courseName),
            datasets: [
                {
                    label: "Overdue Loans",
                    data: rows.map((row) => row.overdueCount),
                    backgroundColor: "#ef4444",
                },
                {
                    label: "Total Owned: £",
                    data: rows.map((row) => row.totalFines),
                    backgroundColor: "#4444ff",
                },
            ],
        };

        loading = false;
    });

    const options = {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            legend: { position: "top" },
        },
        scales: { y: { beginAtZero: true } },
    };
</script>

{#if loading}
    <p>Loading...</p>
{:else}
    <Bar {data} {options} />
{/if}
