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
    import { fetchPopular } from "../../lib/api";
    import BookCard from "../BookCard.svelte";
    import { Bar } from "svelte-chartjs";
    import { filters } from "../../lib/stores";

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
        const rows = await fetchPopular(evt.startDate, evt.endDate);
        data = {
            labels: rows.map((row) => row.title),
            datasets: [
                {
                    label: "Times Borrowed",
                    data: rows.map((row) => row.loanCount),
                    backgroundColor: "#981dae",
                },
            ],
        };
        loading = false;
    }
    $: load($filters);

    const options = {
        indexAxis: "y",
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            legend: { position: "top" },
        },
        scales: { x: { beginAtZero: true } },
    };
</script>

{#if loading}
    <p>Loading...</p>
{:else}
    <Bar {data} {options} />
{/if}
