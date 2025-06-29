import type { Actions, PageServerLoad } from './$types';
import { fail, redirect } from '@sveltejs/kit';
import { SubmitContentStore } from '$houdini';

export const load: PageServerLoad = async ({ locals }) => {
    const session = await locals.auth();

    if (!session) {
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

        if (!session) {
            throw redirect(302, '/auth/signin/keycloak');
        }

        const data = await request.formData();
        const url = data.get('url')?.toString();

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

        const submitContent = new SubmitContentStore();
        await submitContent.mutate({ url });
    }
};
