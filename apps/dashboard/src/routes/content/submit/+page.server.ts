import type { Actions, PageServerLoad } from './$types';
import { fail, redirect } from '@sveltejs/kit';

export const load: PageServerLoad = async ({ locals }) => {
    const session = await locals.auth();
    if (!session?.user) {
        throw redirect(302, '/auth/signin/keycloak');
    }

    return {
        session
    };
};

export const actions: Actions = {
    submit: async ({ request, locals }) => {
        // Check if user is authenticated
        const session = await locals.auth();
        if (!session?.user) {
            throw redirect(302, '/signin');
        }

        const data = await request.formData();
        const url = data.get('url')?.toString();
        const description = data.get('description')?.toString();

        // Validation
        if (!url) {
            return fail(400, { message: 'URL is required' });
        }

        // Basic URL validation
        try {
            const urlObj = new URL(url);
            if (urlObj.protocol !== 'http:' && urlObj.protocol !== 'https:') {
                return fail(400, { message: 'URL must use http:// or https://' });
            }
        } catch {
            return fail(400, { message: 'Invalid URL format' });
        }

        // TODO: Replace this with actual backend API call
        console.log('Submitting content:', {
            url,
            description,
            userId: session.user.id || session.user.email
        });

        // For now, we'll just simulate success
        // In a real implementation, you would:
        // 1. Send the URL to your backend scraping service
        // 2. Store the submission in your database
        // 3. Return appropriate success/error responses

        try {
            // Simulate API call delay
            await new Promise(resolve => setTimeout(resolve, 1000));

            // Simulate successful submission
            return {
                success: true,
                message: 'Content submitted successfully!'
            };
        } catch (error) {
            console.error('Failed to submit content:', error);
            return fail(500, { message: 'Failed to submit content. Please try again.' });
        }
    }
};
