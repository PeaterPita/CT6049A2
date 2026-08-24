<script>
    import {
        BarElement,
        CategoryScale,
        Chart,
        Legend,
        LinearScale,
        Tooltip,
    } from "chart.js";
    import { fetchPeakDays } from "../../lib/api";
    import { colors, filters } from "../../lib/stores";
    import { Bar } from "svelte-chartjs";

    Chart.register(BarElement, CategoryScale, LinearScale, Tooltip, Legend);

    let data = { labels: [], datasets: [] };
    let loading = true;

    async function load(evt) {
        loading = true;
        const rows = await fetchPeakDays(evt.startDate, evt.endDate);
        data = {
            labels: rows.map((row) => row.dayName),
            datasets: [
                {
                    label: "Loans",
                    data: rows.map((row) => row.loanCount),
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
        plugins: { legend: { display: false } },
    };
</script>

{#if loading}
    <p>Loading...</p>
{:else}
    <Bar {data} {options} />
{/if}
