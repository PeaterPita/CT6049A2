import { authToken } from "./stores";

const API_BASE = 'http://localhost:20550';
let token;

authToken.subscribe(value => {
    token = value;
})

async function request(path, opts = {}) {

    const url = API_BASE + path;
    const headers = opts.headers ? { ...opts.headers } : {};


    if (token) headers['Authorization'] = 'Bearer ' + token;
    headers['Content-Type'] = headers['Content-Type'] || 'application/json';


    const res = await fetch(url, { ...opts, headers });
    if (res.status === 401) {
        authToken.set(null);
        throw new Error("Unauthorized");
    }

    let data;
    const contentType = res.headers.get('content-type') || '';
    if (contentType.includes('application/json')) {
        data = await res.json();
    } else {
        data = await res.text();
    }

    if (!res.ok) {
        const err = (typeof data === 'object' && data.error) ? data.error : (data || res.statusText);
        throw new Error(err);
    }

    return data;
}



export async function login(username, password) {
    const res = await fetch(API_BASE + '/api/auth/login', {
        method: 'POST',
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password })
    });
    if (!res.ok) {
        const err = await res.json().catch(() => ({ message: 'Login failed' }));
        throw new Error(err.message || 'bad');
    }
    const body = await res.json();
    return body.token;
}

export async function fetchBooks() {
    return request('/api/books');
}

export async function searchBooks(query = '', available = false) {
    const urlBuild = new URLSearchParams();
    if (query && query.trim().length > 0) urlBuild.append("query", query.trim());
    if (available) urlBuild.append('available', 'true');

    const path = '/api/books' + (urlBuild.toString() ? ('?' + urlBuild.toString()) : '');
    return request(path);
}

export async function fetchBookById(id) {
    return request(`/api/books/${id}`);
}



export async function fetchLoansForUser() {
    return request('/api/loans/me');


}

export async function borrowBook(bookId) {
    return request('/api/loans/borrow', {
        method: 'POST',
        body: JSON.stringify({ bookId })
    });
}

export async function returnBook(loanId) {
    return request('/api/loans/return', {
        method: 'POST',
        body: JSON.stringify({ loanId })
    });
}

export async function payFine(loanId) {
    return request(`/api/loans/pay/${loanId}`, { method: 'POST' });
}


export async function startEtl() {
    return request('/api/etl', {
        method: 'POST'
    })
}



export async function listBackups() {
    return request('/api/backup');


}

export async function createBackup() {
    return request('/api/backup', { method: "POST" });
}

export async function restoreBackup(filename) {
    return request(`/api/backup/activate/${filename}`, { method: 'POST' })
}




export async function fetchLoanTrend(startDate, endDate) {

    let params = new URLSearchParams();
    if (startDate) params.append("start", startDate);
    if (endDate) params.append("end", endDate);

    let added = params.toString() ? "?" + params.toString() : "";

    return request('/api/warehouse/loans/trend' + added);
}

export async function fetchOverdueSummary() {
    return request('/api/warehouse/loans/overdue');
}


export async function fetchPopular(startDate, endDate) {


    let params = new URLSearchParams();
    if (startDate) params.append("start", startDate);
    if (endDate) params.append("end", endDate);

    let added = params.toString() ? "?" + params.toString() : "";



    return request('/api/warehouse/books/popular' + added);
}


export async function fetchCourseEngagment(startDate, endDate) {


    let params = new URLSearchParams();
    if (startDate) params.append("start", startDate);
    if (endDate) params.append("end", endDate);

    let added = params.toString() ? "?" + params.toString() : "";



    return request('/api/warehouse/loans/course' + added);
}


export async function fetchFineRevenue(startDate, endDate) {



    let params = new URLSearchParams();
    if (startDate) params.append("start", startDate);
    if (endDate) params.append("end", endDate);

    let added = params.toString() ? "?" + params.toString() : "";




    return request('/api/warehouse/fines/trend' + added);
}

export async function fetchPeakDays(startDate, endDate) {

    let params = new URLSearchParams();
    if (startDate) params.append("start", startDate);
    if (endDate) params.append("end", endDate);

    let added = params.toString() ? "?" + params.toString() : "";




    return request('/api/warehouse/loans/peak' + added)
}

export async function fetchOverstockedBooks(startDate, endDate) {
    let res = request('/api/warehouse/books/overstocked');

    console.log(res);

    return res;
}

export async function fetchLoanDurationByCourse(startDate, endDate) {


    let params = new URLSearchParams();
    if (startDate) params.append("start", startDate);
    if (endDate) params.append("end", endDate);

    let added = params.toString() ? "?" + params.toString() : "";


    return request('/api/warehouse/loans/duration' + added)
}







export async function fetchYearEngagment(startDate, endDate) {


    let params = new URLSearchParams();
    if (startDate) params.append("start", startDate);
    if (endDate) params.append("end", endDate);

    let added = params.toString() ? "?" + params.toString() : "";



    return request('/api/warehouse/loans/year' + added);
}






















































