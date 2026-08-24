<script>
    import { push } from "svelte-spa-router";
    import { login } from "../lib/api";
    import { authToken } from "../lib/stores";

    let username = "user";
    let password = "password";
    let error = "";
    let loading = false;

    async function onSubmit(event) {
        event.preventDefault();
        error = "";
        loading = true;
        try {
            const token = await login(username, password);

            authToken.set(token);

            push("/");
        } catch (err) {
            error = err.message || "Failed to login. Please check creds";
        } finally {
            loading = false;
        }
    }
</script>

<div class="panel login-form">
    <h2>Login</h2>
    <form on:submit={onSubmit}>
        <label> Username <input bind:value={username} /> </label>

        <label> Password <input bind:value={password} type="password" /></label>

        <div style="margin-top:10px;">
            <button type="submit" disabled={loading}
                >{loading ? "Logging in..." : "Login"}</button
            >
        </div>

        {#if error}
            <p style="color:red;">{error}</p>
        {/if}
    </form>
</div>
