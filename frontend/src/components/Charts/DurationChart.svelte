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
    import { fetchLoanDurationByCourse } from "../../lib/api";
    import { Bar } from "svelte-chartjs";
    import { colors, filters } from "../../lib/stores";

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

    async function load(evt) {
        loading = true;
        const rows = await fetchLoanDurationByCourse(
            evt.startDate,
            evt.endDate,
        );
        data = {
            labels: rows.map((row) => row.courseName),
            datasets: [
                {
                    label: "Average Loan duration",
                    data: rows.map((row) => row.avg),
                    backgroundColor: colors,
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
            legend: { pisition: "top" },
        },
        scales: { y: { beginAtZero: true } },
    };
</script>

{#if loading}
    <p>loading...</p>
{:else}
    <Bar {data} {options} />
{/if}
