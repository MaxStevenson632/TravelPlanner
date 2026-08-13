// Helper function to sanitize user input - prevent XSS attack
export function sanitizedHTML(str) {

    if (!str) {
        return '';
    }
    return str.replace(/[&<>"']/g, match => ({
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#39;'
    }[match]));
}