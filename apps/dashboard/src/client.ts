import { HoudiniClient } from '$houdini';
import { CONTENT_API_URL } from '$env/static/private';

export default new HoudiniClient({
    url: CONTENT_API_URL,
    fetchParams({ session }) {
        return {
            headers: {
                Authorization: session?.accessToken ? `Bearer ${session?.accessToken}` : '',
            }
        }
    }
})
