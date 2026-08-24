<script>
    import { borrowBook, searchBooks } from "../lib/api";
    import { onMount } from "svelte";
    import BookCard from "./BookCard.svelte";
    import { addAlert } from "../lib/stores";

    const DEBOUNCE_TIME_MS = 500;

    let availableOnly = false;
    let books = [];
    let loading = true;
    let debounceTimer = null;
    let query = "";

    async function load() {
        loading = true;
        try {
            books = await searchBooks(query, availableOnly);
        } catch (err) {
            addAlert(err.message);
        } finally {
            loading = false;
        }
    }

    onMount(load);

    function onQueryChange(i) {
        query = i.target.value;
        if (debounceTimer) clearTimeout(debounceTimer);
        debounceTimer = setTimeout(() => load(), DEBOUNCE_TIME_MS);
    }

    async function handleLoanOut(bookId, bookTitle) {
        try {
            await borrowBook(bookId);
            addAlert(`Book borrowed: ${bookTitle}`, "success");
            await load();
        } catch (err) {
            addAlert(err.message, "error");
        }
    }
</script>

<div>
    <h2>Browse Books</h2>

    <div class="controls">
        <input
            placeholder="Search by title or author..."
            value={query}
            on:input={onQueryChange}
        />
        <label
            ><input
                type="checkbox"
                bind:checked={availableOnly}
                on:change={load}
            /> Available only</label
        >
    </div>

    {#if loading}
        <p class="center">Loading books...</p>
    {:else if books.length === 0}
        <p class="center">No books found</p>
    {:else}
        <div class="grid">
            {#each books as book}
                <BookCard
                    {book}
                    showActions={true}
                    handleLoan={handleLoanOut}
                />
            {/each}
        </div>
    {/if}
</div>

<style>
    .controls {
        display: flex;
        gap: 10px;
        align-items: center;
        margin-bottom: 1rem;
    }
    .center {
        text-align: center;
        color: #666;
    }
</style>
