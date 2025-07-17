import { HoudiniClient } from '$houdini';
import { env } from '$env/dynamic/public';
import type { Session } from '@auth/core/types';

// Get configuration from environment (now supports runtime values)
const contentApiUrl = env.PUBLIC_API_URL!;

export default new HoudiniClient({
    url: contentApiUrl,
    fetchParams({ session }: { session?: Session }) {
        return {
            headers: {
                Authorization: session?.accessToken ? `Bearer ${session?.accessToken}` : '',
            }
        }
    }
})
