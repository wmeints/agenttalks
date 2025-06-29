import type { ContentQueryVariables } from "./$houdini";

export const _ContentQueryVariables: ContentQueryVariables = () => (
    {
        pageIndex: 0,
        pageSize: 10,
    }
);

// export const load: PageLoad = async (event) => {
//     console.log("Loading content items...");

//     return {
//         ...await load_ContentQuery({ event, variables: { pageIndex: 0, pageSize: 10 } }),
//     }
// };
