<script>
    export let title = "";
</script>

<div class="card">
    {#if title}
        <h3>{title}</h3>
    {/if}
    <div style="height: 280px">
        <slot />
    </div>
</div>

<style>
</style>
