/* eslint-disable */
import * as types from './graphql';
import type { TypedDocumentNode as DocumentNode } from '@graphql-typed-document-node/core';

/**
 * Map of all GraphQL operations in the project.
 *
 * This map has several performance disadvantages:
 * 1. It is not tree-shakeable, so it will include all operations in the project.
 * 2. It is not minifiable, so the string of a GraphQL query will be multiple times inside the bundle.
 * 3. It does not support dead code elimination, so it will add unused operations.
 *
 * Therefore it is highly recommended to use the babel or swc plugin for production.
 * Learn more about it here: https://the-guild.dev/graphql/codegen/plugins/presets/preset-client#reducing-bundle-size
 */
type Documents = {
    "\n  query fetchEpisodesQuery($pageIndex: Int!, $pageSize: Int!) {\n    episodes(pageIndex: $pageIndex, pageSize: $pageSize) {\n      totalPages\n      totalItems\n      items {\n        id\n        title\n      }\n    }\n  }\n": typeof types.FetchEpisodesQueryDocument,
    "\n  query fetchPendingSubmissionsQuery {\n    pendingSubmissions {\n        id\n        title\n        url\n        summary\n    }\n  }\n": typeof types.FetchPendingSubmissionsQueryDocument,
    "\n  query fetchSubmissionsQuery($pageIndex: Int!, $pageSize: Int!) {\n    submissions(pageIndex: $pageIndex, pageSize: $pageSize) {\n      totalPages\n      totalItems\n      items {\n        id\n        title\n        url\n        summary\n      }\n    }\n  }\n": typeof types.FetchSubmissionsQueryDocument,
    "\n  query fetchStatisticsQuery {\n    statistics {\n      pendingSubmissions\n      totalEpisodes\n    }\n  }\n": typeof types.FetchStatisticsQueryDocument,
    "\n  mutation submitContentMutation ($url: String!, $instructions: String!) {\n    submitContent(input: {url: $url, instructions: $instructions }) {\n      id\n    }\n  }": typeof types.SubmitContentMutationDocument,
};
const documents: Documents = {
    "\n  query fetchEpisodesQuery($pageIndex: Int!, $pageSize: Int!) {\n    episodes(pageIndex: $pageIndex, pageSize: $pageSize) {\n      totalPages\n      totalItems\n      items {\n        id\n        title\n      }\n    }\n  }\n": types.FetchEpisodesQueryDocument,
    "\n  query fetchPendingSubmissionsQuery {\n    pendingSubmissions {\n        id\n        title\n        url\n        summary\n    }\n  }\n": types.FetchPendingSubmissionsQueryDocument,
    "\n  query fetchSubmissionsQuery($pageIndex: Int!, $pageSize: Int!) {\n    submissions(pageIndex: $pageIndex, pageSize: $pageSize) {\n      totalPages\n      totalItems\n      items {\n        id\n        title\n        url\n        summary\n      }\n    }\n  }\n": types.FetchSubmissionsQueryDocument,
    "\n  query fetchStatisticsQuery {\n    statistics {\n      pendingSubmissions\n      totalEpisodes\n    }\n  }\n": types.FetchStatisticsQueryDocument,
    "\n  mutation submitContentMutation ($url: String!, $instructions: String!) {\n    submitContent(input: {url: $url, instructions: $instructions }) {\n      id\n    }\n  }": types.SubmitContentMutationDocument,
};

/**
 * The graphql function is used to parse GraphQL queries into a document that can be used by GraphQL clients.
 *
 *
 * @example
 * ```ts
 * const query = graphql(`query GetUser($id: ID!) { user(id: $id) { name } }`);
 * ```
 *
 * The query argument is unknown!
 * Please regenerate the types.
 */
export function graphql(source: string): unknown;

/**
 * The graphql function is used to parse GraphQL queries into a document that can be used by GraphQL clients.
 */
export function graphql(source: "\n  query fetchEpisodesQuery($pageIndex: Int!, $pageSize: Int!) {\n    episodes(pageIndex: $pageIndex, pageSize: $pageSize) {\n      totalPages\n      totalItems\n      items {\n        id\n        title\n      }\n    }\n  }\n"): (typeof documents)["\n  query fetchEpisodesQuery($pageIndex: Int!, $pageSize: Int!) {\n    episodes(pageIndex: $pageIndex, pageSize: $pageSize) {\n      totalPages\n      totalItems\n      items {\n        id\n        title\n      }\n    }\n  }\n"];
/**
 * The graphql function is used to parse GraphQL queries into a document that can be used by GraphQL clients.
 */
export function graphql(source: "\n  query fetchPendingSubmissionsQuery {\n    pendingSubmissions {\n        id\n        title\n        url\n        summary\n    }\n  }\n"): (typeof documents)["\n  query fetchPendingSubmissionsQuery {\n    pendingSubmissions {\n        id\n        title\n        url\n        summary\n    }\n  }\n"];
/**
 * The graphql function is used to parse GraphQL queries into a document that can be used by GraphQL clients.
 */
export function graphql(source: "\n  query fetchSubmissionsQuery($pageIndex: Int!, $pageSize: Int!) {\n    submissions(pageIndex: $pageIndex, pageSize: $pageSize) {\n      totalPages\n      totalItems\n      items {\n        id\n        title\n        url\n        summary\n      }\n    }\n  }\n"): (typeof documents)["\n  query fetchSubmissionsQuery($pageIndex: Int!, $pageSize: Int!) {\n    submissions(pageIndex: $pageIndex, pageSize: $pageSize) {\n      totalPages\n      totalItems\n      items {\n        id\n        title\n        url\n        summary\n      }\n    }\n  }\n"];
/**
 * The graphql function is used to parse GraphQL queries into a document that can be used by GraphQL clients.
 */
export function graphql(source: "\n  query fetchStatisticsQuery {\n    statistics {\n      pendingSubmissions\n      totalEpisodes\n    }\n  }\n"): (typeof documents)["\n  query fetchStatisticsQuery {\n    statistics {\n      pendingSubmissions\n      totalEpisodes\n    }\n  }\n"];
/**
 * The graphql function is used to parse GraphQL queries into a document that can be used by GraphQL clients.
 */
export function graphql(source: "\n  mutation submitContentMutation ($url: String!, $instructions: String!) {\n    submitContent(input: {url: $url, instructions: $instructions }) {\n      id\n    }\n  }"): (typeof documents)["\n  mutation submitContentMutation ($url: String!, $instructions: String!) {\n    submitContent(input: {url: $url, instructions: $instructions }) {\n      id\n    }\n  }"];

export function graphql(source: string) {
  return (documents as any)[source] ?? {};
}

export type DocumentType<TDocumentNode extends DocumentNode<any, any>> = TDocumentNode extends DocumentNode<  infer TType,  any>  ? TType  : never;