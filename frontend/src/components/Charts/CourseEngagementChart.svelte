<script>
    import {
        ArcElement,
        Chart,
        DoughnutController,
        Legend,
        Tooltip,
    } from "chart.js";
    import { fetchCourseEngagment } from "../../lib/api";
    import { colors, filters } from "../../lib/stores";
    import { Doughnut } from "svelte-chartjs";

    Chart.register(ArcElement, DoughnutController, Tooltip, Legend);

    let data = { labels: [], datasets: [] };
    let loading = true;

    async function load(evt) {
        loading = true;
        const rows = await fetchCourseEngagment(evt.startDate, evt.endDate);
        data = {
            labels: rows.map((row) => row.courseName),
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
