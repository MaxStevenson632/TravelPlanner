export const token = localStorage.getItem('token');

export function getAccountNameFromToken() {

    try {

        // Split the token apart, use the payload (second part)
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');

        // Decode JSON string
        const jsonPayload = decodeURIComponent(
            atob(base64)
                .split('')
                .map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
                .join('')
        );

        const payload = JSON.parse(jsonPayload);

        return payload.name;

    } catch (error) {
        console.error("Failed to decode JWT:", error);
        return null;
    }
}