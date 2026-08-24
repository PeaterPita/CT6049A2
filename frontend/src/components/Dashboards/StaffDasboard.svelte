<script>
    import { startEtl } from "../../lib/api";
    import { userRole, addAlert, filters } from "../../lib/stores";
    import BackupPanel from "../BackupPanel.svelte";
    import ChartCard from "../ChartCard.svelte";
    import CourseEngagementChart from "../Charts/CourseEngagementChart.svelte";
    import DurationChart from "../Charts/DurationChart.svelte";
    import FineRevChart from "../Charts/FineRevChart.svelte";
    import LoanChart from "../Charts/LoanChart.svelte";
    import OverdueChart from "../Charts/OverdueChart.svelte";
    import PeakDaysChart from "../Charts/PeakDaysChart.svelte";
    import PopularChart from "../Charts/PopularChart.svelte";
    import YearEngagement from "../Charts/YearEngagement.svelte";

    let startDate = "";
    let endDate = "";

    filters.subscribe((evt) => {
        startDate = evt.startDate;
        endDate = evt.endDate;
    });

    async function handleFilter() {
        addAlert("Changed filters", "success");
        filters.set({ startDate, endDate });
    }

    async function handleEtl() {
        try {
            addAlert(`ETL Process started..`, `success`);
            await startEtl();
            addAlert(`Finished ETL`, `success`);

            // This isnt ideal. Should probably do a key block or load data in this parent
            window.location.reload();
        } catch (err) {
            addAlert(err.message);
        }
    }
</script>

<div>
    <label> From: <input type="date" bind:value={startDate} /></label>
    <label> To: <input type="date" bind:value={endDate} /></label>

    <button on:click={() => handleFilter()}>Apply</button>
</div>

<div class="dashboard-grid">
    <div style="grid-column: span 3">
        <ChartCard title="Loans Trend Over Time">
            <LoanChart />
        </ChartCard>
    </div>

    {#if $userRole == "ADMIN"}
        <ChartCard title="Admin Controls">
            <button on:click={() => handleEtl()}>Load ETL</button>
            <BackupPanel />
        </ChartCard>
    {/if}

    <div style="grid-column: span 2">
        <ChartCard title="Engagment by course">
            <CourseEngagementChart />
        </ChartCard>
    </div>
    <div style="grid-column: span 2">
        <ChartCard title="Ongoing Fines By course">
            <OverdueChart />
        </ChartCard>
    </div>

    <div style="grid-column: span 3">
        <ChartCard title="Avg Duration (days)">
            <DurationChart />
        </ChartCard>
    </div>

    <ChartCard title="Engagment by Year">
        <YearEngagement />
    </ChartCard>

    <ChartCard title="Loans Taken out on days of the week">
        <PeakDaysChart />
    </ChartCard>

    <div style="grid-column: span 3">
        <ChartCard title="Top Revenue From Fine">
            <FineRevChart />
        </ChartCard>
    </div>

    <div class="full">
        <ChartCard title="Top 10 Loaned books">
            <PopularChart />
        </ChartCard>
    </div>
</div>

<style>
    .dashboard-grid {
        display: grid;
        grid-template-columns: repeat(4, 1fr);
        gap: 1rem;
        padding: 1.5rem;
        min-height: 100vh;
    }

    .full {
        grid-column: 1 / -1;
    }
</style>
