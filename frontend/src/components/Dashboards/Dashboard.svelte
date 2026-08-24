<script>
    import { userRole } from "../../lib/stores";
    import StaffDasboard from "./StaffDasboard.svelte";
    import StudentDashboard from "./StudentDashboard.svelte";
</script>

{#if $userRole === "STUDENT"}
    <StudentDashboard />
{:else if ["ADMIN", "DECISION_MAKER"].includes($userRole)}
    <StaffDasboard />
{:else}
    <p>Loading...</p>
{/if}
