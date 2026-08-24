<script>
    import Router, { push } from "svelte-spa-router";
    import { authToken } from "./lib/stores";

    import Login from "./components/Login.svelte";
    import Dashboard from "./components/Dashboards/Dashboard.svelte";
    import Browse from "./components/Browse.svelte";
    import Navbar from "./components/Navbar.svelte";
    import Alert from "./components/Alert.svelte";

    const routes = {
        "/": Dashboard,
        "/login": Login,
        "/browse": Browse,
    };

    $: if (!$authToken) {
        push("/login");
    }
</script>

<nav>
    {#if $authToken}
        <Navbar />
    {/if}
</nav>

<Alert />
<Router {routes} />
