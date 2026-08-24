<script>
    import { alerts } from "../lib/stores";
</script>

<div class="alert-container">
    {#each $alerts as alert (alert.id)}
        <div class="alert {alert.style}">
            <span>
                {alert.message}
            </span>
        </div>
    {/each}
</div>
