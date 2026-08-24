<script>
    export let loans = [];
    export let onReturn;

    let startDate = "";
    let endDate = "";

    $: loansHistory = loans
        .filter((loan) => {
            if (!startDate && !endDate) return true;

            const loanDate = new Date(loan.loanDate);
            const start = startDate
                ? new Date(startDate)
                : new Date("2010-01-01");
            const end = endDate ? new Date(endDate) : new Date(Date.now());

            return loanDate >= start && loanDate <= end;
        })
        .sort((l1, l2) => {
            return (
                new Date(l2.loanDate).getTime() -
                new Date(l1.loanDate).getTime()
            );
        });

    $: totalPaid = loansHistory
        .filter((loan) => loan.status === "PAID")
        .reduce((sum, loan) => sum + (loan.fine || 0), 0);

    function genTags(loan) {
        let tagList = [];

        if (loan.returnDate) {
            tagList.push({ text: "Returned", style: "returned" });

            if (new Date(loan.returnDate) > new Date(loan.dueDate)) {
                tagList.push({ text: "Late", style: "late" });
            }
        } else if (loan.status === "OVERDUE") {
            tagList.push({ text: "Overdue", style: "late" });
        } else {
            tagList.push({ text: "Borrowed", style: "active" });
        }
        return tagList;
    }
</script>

<div class="panel history-panel">
    <div class="panel-header">
        <div class="header-left">
            <h2>Loan History</h2>
            <div class="total-badge">
                {#if totalPaid > 0}
                    <span>Total paid: £{totalPaid.toFixed(2)}</span>
                {/if}
                <span
                    >Fines paid: {loansHistory.filter(
                        (loan) => loan.status === "PAID",
                    ).length}</span
                >
            </div>
        </div>
        <div class="date-controls">
            <label>
                <span>From:</span>
                <input type="date" bind:value={startDate} />
            </label>
            <label>
                <span>To:</span>
                <input type="date" bind:value={endDate} />
            </label>
        </div>
    </div>

    <div class="table-container">
        <table>
            <thead>
                <tr>
                    <th>Book (Total: {loansHistory.length})</th>
                    <th>Loaned</th>
                    <th>Due</th>
                    <th>Status</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                {#each loansHistory as loan}
                    <tr>
                        <td>{loan.book ? loan.book.title : "Error"}</td>
                        <td>{loan.loanDate}</td>
                        <td>{loan.dueDate}</td>
                        <td>
                            <div class="tagRow">
                                {#each genTags(loan) as tag}
                                    <span class="tag {tag.style}"
                                        >{tag.text}</span
                                    >
                                {/each}
                            </div>
                        </td>
                        <td>
                            {#if !loan.returnDate && loan.status != "OVERDUE"}
                                <button
                                    class="btn"
                                    on:click={() => onReturn(loan.id)}
                                    >Return</button
                                >
                            {:else if loan.status === "PAID"}
                                <span>£{loan.fine.toFixed(2)} Paid</span>
                            {:else if !loan.returnDate && loan.status === "OVERDUE"}
                                <span class="tag late">Pay Fine to Return</span>
                            {:else}{/if}
                        </td>
                    </tr>
                {:else}
                    <tr
                        ><td colspan="5" style="text-align:center"
                            >No history available</td
                        ></tr
                    >
                {/each}
            </tbody>
        </table>
    </div>
</div>
