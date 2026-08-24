<script>
    export let book;
    export let showActions = false;
    export let showDue = false;
    export let handleLoan = (_1, _2) => {};
</script>

<!-- svelte-ignore a11y_click_events_have_key_events -->
<!-- svelte-ignore a11y_no_static_element_interactions -->
<div class="active-card">
    <img src={book.coverURL} alt="Cover" />
    <div class="book-meta">
        <div class="book-title">{book.title}</div>
        <div class="book-author">{book.author}</div>

        {#if showActions}
            <div class="book-copies">
                {book.copiesLeft
                    ? `Copies: ${book.copiesLeft}`
                    : "Out of Stock"}
            </div>

            <button
                class="loanBtn"
                disabled={book.copiesLeft <= 0}
                on:click={() => handleLoan(book.id, book.title)}>Loan</button
            >
        {:else if showDue}
            <span class="label">Return by: {book.dueDate}</span>
        {/if}
    </div>
</div>
