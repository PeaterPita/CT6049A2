<script>
    import {
        BarElement,
        CategoryScale,
        Chart,
        Legend,
        LinearScale,
        Tooltip,
    } from "chart.js";
    import BookCard from "../BookCard.svelte";
    import { fetchFineRevenue } from "../../lib/api";
    import { color } from "chart.js/helpers";
    import { colors, filters } from "../../lib/stores";
    import { Bar } from "svelte-chartjs";

    Chart.register(BarElement, CategoryScale, Legend, LinearScale, Tooltip);

    let data = { labels: [], datasets: [] };
    let loading = true;

    async function load(evt) {
        loading = true;
        const rows = await fetchFineRevenue(evt.startDate, evt.endDate);

        const months = [
            ...new Set(rows.map((row) => `${row.monthName} ${row.year}`)),
        ];
        const courses = [...new Set(rows.map((row) => row.courseName))];

        const datasets = courses.map((course, i) => ({
            label: course,
            data: months.map((month) => {
                const row = rows.find(
                    (row) =>
                        `${row.monthName} ${row.year}` === month &&
                        row.courseName === course,
                );
                return row ? row.totalFines : 0;
            }),
            backgroundColor: colors,
        }));

        data = { labels: months, datasets };
        loading = false;
    }
    $: load($filters);

    const options = {
        responsive: true,
        maintainAspectRatio: false,
        scales: { x: { stacked: true }, y: { stacked: true } },
        plugins: { Legend: { display: false, position: "flase" } },
    };
</script>

{#if loading}
    <p>Loading...</p>
{:else}
    <Bar {data} {options} />
{/if}
