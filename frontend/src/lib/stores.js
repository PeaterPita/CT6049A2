import { derived, writable } from "svelte/store";


export const authToken = writable(localStorage.getItem('jwt_token'));
authToken.subscribe(value => {
    if (value) localStorage.setItem('jwt_token', value);
    else localStorage.removeItem('jwt_token')
});

export const alerts = writable([]);

export const filters = writable({
    startDate: new Date(Date.now() - 3600 * 24 * 365 * 1000).toISOString().slice(0, 10),
    endDate: new Date().toISOString().slice(0, 10),
})

export function addAlert(message, style) {
    const id = Math.random();

    alerts.update((prev) => [
        ...prev,
        { id, message, style }
    ]);

    setTimeout(() => {
        removeAlert(id);
    }, 5000);
}

function removeAlert(id) {
    alerts.update((prev) => prev.filter((alert) => alert.id !== id));
}



export const userRole = derived(authToken, ($auth) => {
    try {
        const payload = JSON.parse(atob($auth.split(".")[1]));
        return payload.role || null;
    } catch (err) {
        console.error("Error with the token role fetch");
        console.error(err);
        return null
    }
})


export const currentUser = derived(authToken, ($auth) => {
    try {
        const payload = JSON.parse(atob($auth.split(".")[1]));
        return payload.sub || "ERROR";
    } catch (err) {
        console.error("Error with the token username fetch");
        console.error(err);
        return null;
    }
});



export const colors = [
    "#5F0F40",
    "#321325",
    "#9A031E",
    "#CB793A",
    "#0D0630",
    "#1A5E63",
    "#59A96A",
    "#531253",
    "#6B8F71",
    "#706993",
    "#D5B942",
    "#EE7674",
];
