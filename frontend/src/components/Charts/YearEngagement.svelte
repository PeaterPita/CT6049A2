<script>
    import {
        ArcElement,
        Chart,
        DoughnutController,
        Legend,
        Tooltip,
    } from "chart.js";
    import { onMount } from "svelte";
    import { fetchYearEngagment } from "../../lib/api";
    import { Doughnut } from "svelte-chartjs";
    import { colors, filters } from "../../lib/stores";

    Chart.register(ArcElement, DoughnutController, Tooltip, Legend);

    let data = { labels: [], datasets: [] };
    let loading = true;

    let years = { 1: "1st Year", 2: "2nd Year", 3: "3rd Year (last)" };

    async function load(evt) {
        loading = true;
        const rows = await fetchYearEngagment(evt.startDate, evt.endDate);
        data = {
            labels: rows.map(
                (row) => years[row.studyYear] ?? `Year ${row.studyYear}`,
            ),
            datasets: [
                {
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
        plugins: {
            legened: { position: "right" },
        },
    };
</script>

{#if loading}
    <p>Loading...</p>
{:else}
    <Doughnut {data} {options} />
{/if}
