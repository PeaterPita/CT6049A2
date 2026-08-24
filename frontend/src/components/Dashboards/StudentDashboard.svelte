<script>
    import { push } from "svelte-spa-router";
    import { fetchLoansForUser, payFine, returnBook } from "../../lib/api";
    import { onMount } from "svelte";
    import BookCard from "../BookCard.svelte";
    import LoanPanel from "../loanPanel.svelte";
    import { addAlert } from "../../lib/stores";

    let loans = [];
    let loading = true;

    async function handlePay(loanId, fineAmount) {
        if (!confirm(`Are you sure?\nPayment Due: £${fineAmount}`)) return;
        try {
            await payFine(loanId);
            addAlert(`Payment of £${fineAmount} complete!`, "success");
            await load();
        } catch (err) {
            addAlert(err.message);
        }
    }
    async function loanReturn(loanId) {
        try {
            await returnBook(loanId);
            addAlert(`Book returned`, "success");
            await load();
        } catch (err) {
            addAlert(err.message);
        }
    }
    async function load() {
        try {
            loans = await fetchLoansForUser();
        } catch (err) {
            addAlert(err.message);
            if (err.message === "Unauthorized") {
                push("/login");
            }
        } finally {
            loading = false;
        }
    }

    onMount(() => {
        load();
    });

    $: currentLoans = loans.filter((loan) => !loan.returnDate);
    $: finesList = loans.filter((loan) => loan.status === "OVERDUE");
</script>

<section>
    <h3>Reading List</h3>

    {#if currentLoans.length === 0}
        <p>No books currently on loan!</p>
    {:else}
        <div class="grid">
            {#each currentLoans as loan}
                <BookCard
                    book={{ ...loan.book, dueDate: loan.dueDate }}
                    showDue={true}
                />
            {/each}
        </div>
    {/if}
</section>

<hr />

<div class="split-view">
    <LoanPanel {loans} onReturn={loanReturn} />

    <div class="panel fines-panel">
        <h2>
            Fines

            <span class="label">Additional £1.50 per day overdue</span>
        </h2>

        <div class="fines-list">
            {#each finesList as fine}
                <div class="fine-item">
                    <div class="fine-details">
                        <strong>{fine.book ? fine.book.title : "Error"}</strong>
                        <small
                            >Due: {fine.dueDate}
                            {fine.returnDate
                                ? `(Return: ${fine.returnDate})`
                                : ""}</small
                        >
                    </div>
                    <div class="fine-action">
                        <span class="amount">£{fine.fine.toFixed(2)}</span>
                        <button
                            class="pay-btn"
                            on:click={() =>
                                handlePay(fine.id, fine.fine.toFixed(2))}
                            >Pay</button
                        >
                    </div>
                </div>
            {:else}
                <div>
                    <p>No fines need to be paid</p>
                </div>
            {/each}
        </div>
    </div>
</div>
