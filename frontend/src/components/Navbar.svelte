<script>
    import { link } from "svelte-spa-router";
    import { authToken, currentUser, userRole } from "../lib/stores";

    function logout() {
        authToken.set(null);
    }
</script>

<nav class="navbar">
    <div class="container nav-container">
        <a use:link href="/" class="nav-brand">DBA2<span>Library</span></a>
        <div class="nav-links">
            {#if $userRole === "STUDENT"}
                <a use:link href="/browse" class="nav-item">Browse</a>
            {/if}

            <div class="ctaMenu">
                <button on:click={logout} class="nav-logout">Logout</button>
                {#if $currentUser}
                    <div class="label">
                        Logged in as: <strong>{$currentUser}</strong>
                    </div>
                {/if}
            </div>
        </div>
    </div>
</nav>
