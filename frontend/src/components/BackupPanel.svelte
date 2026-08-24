<script>
    import { onMount } from "svelte";
    import { createBackup, listBackups, restoreBackup } from "../lib/api";
    import { addAlert } from "../lib/stores";

    let backups = [];
    let loading = true;
    let creating = false;
    let restoringFile = null;

    async function load() {
        try {
            backups = await listBackups();
        } catch (err) {
            addAlert(err.message);
        } finally {
            loading = false;
        }
    }

    async function handleCreate() {
        creating = true;
        try {
            const res = await createBackup();
            addAlert(`Backup created: ${res.filename}`, "success");
            await load();
        } catch (err) {
            addAlert(err.message);
        } finally {
            creating = false;
        }
    }

    async function handleRestore(filename) {
        if (
            !confirm(
                `Restore from {filename}? This will overwrite the current data`,
            )
        )
            return;
        restoringFile = filename;
        try {
            handleCreate();

            await restoreBackup(filename);
            addAlert("Restored from previous backup");
        } catch (err) {
            addAlert(err.message);
        } finally {
            restoringFile = null;
        }
    }

    onMount(load);
</script>

<button class="create-btn" on:click={handleCreate} disabled={creating}
    >{creating ? "In Progres..." : "Create backup"}</button
>

{#if loading}
    <p>Loading...</p>
{:else if backups.length === 0}
    <p>No backups</p>
{:else}
    <table>
        <thead>
            <tr>
                <th>Filename</th>
                <th>Size</th>
                <th>Modified</th>
                <th></th>
            </tr>
        </thead>
        <tbody>
            {#each backups as backup}
                <tr>
                    <td>{backup.filename}</td>
                    <td>{backup.size}</td>
                    <td>{backup.modified}</td>
                    <td>
                        <button
                            on:click={() => handleRestore(backup.filename)}
                            disabled={restoringFile !== null}
                        >
                            {restoringFile === backup.filename
                                ? "Loading..."
                                : "REstore"}
                        </button>
                    </td>
                </tr>
            {/each}
        </tbody>
    </table>
{/if}
