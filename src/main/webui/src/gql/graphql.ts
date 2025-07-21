/* eslint-disable */
import type { TypedDocumentNode as DocumentNode } from '@graphql-typed-document-node/core';
export type Maybe<T> = T | null;
export type InputMaybe<T> = Maybe<T>;
export type Exact<T extends { [key: string]: unknown }> = { [K in keyof T]: T[K] };
export type MakeOptional<T, K extends keyof T> = Omit<T, K> & { [SubKey in K]?: Maybe<T[SubKey]> };
export type MakeMaybe<T, K extends keyof T> = Omit<T, K> & { [SubKey in K]: Maybe<T[SubKey]> };
export type MakeEmpty<T extends { [key: string]: unknown }, K extends keyof T> = { [_ in K]?: never };
export type Incremental<T> = T | { [P in keyof T]?: P extends ' $fragmentName' | '__typename' ? T[P] : never };
/** All built-in and custom scalars, mapped to their actual values */
export type Scalars = {
  ID: { input: string; output: string; }
  String: { input: string; output: string; }
  Boolean: { input: boolean; output: boolean; }
  Int: { input: number; output: number; }
  Float: { input: number; output: number; }
  /** Scalar for BigDecimal */
  BigDecimal: { input: any; output: any; }
  /** Scalar for BigInteger */
  BigInteger: { input: any; output: any; }
  /** Scalar for DateTime */
  DateTime: { input: any; output: any; }
};

export type ApplicationStatistics = {
  __typename?: 'ApplicationStatistics';
  pendingSubmissions: Scalars['BigInteger']['output'];
  totalEpisodes: Scalars['BigInteger']['output'];
};

export type ContentSubmission = {
  __typename?: 'ContentSubmission';
  /** ISO-8601 */
  dateCreated: Scalars['DateTime']['output'];
  /** ISO-8601 */
  dateModified?: Maybe<Scalars['DateTime']['output']>;
  id: Scalars['BigInteger']['output'];
  instructions?: Maybe<Scalars['String']['output']>;
  status: SubmissionStatus;
  summary?: Maybe<Scalars['String']['output']>;
  title?: Maybe<Scalars['String']['output']>;
  url: Scalars['String']['output'];
};

export type ContentSubmissionResultSet = {
  __typename?: 'ContentSubmissionResultSet';
  items: Array<Maybe<ContentSubmission>>;
  pageIndex: Scalars['Int']['output'];
  pageSize: Scalars['Int']['output'];
  totalItems: Scalars['Int']['output'];
  totalPages: Scalars['Int']['output'];
};

/** Mutation root */
export type Mutation = {
  __typename?: 'Mutation';
  /** Submits content for processing */
  submitContent?: Maybe<ContentSubmission>;
};


/** Mutation root */
export type MutationSubmitContentArgs = {
  input?: InputMaybe<SubmitContentRequestInput>;
};

export type PodcastEpisode = {
  __typename?: 'PodcastEpisode';
  audioFilePath: Scalars['String']['output'];
  /** ISO-8601 */
  dateCreated: Scalars['DateTime']['output'];
  description: Scalars['String']['output'];
  episodeNumber: Scalars['Int']['output'];
  id: Scalars['BigInteger']['output'];
  script: PodcastScript;
  summary: Scalars['String']['output'];
  title: Scalars['String']['output'];
};

export type PodcastEpisodeResultSet = {
  __typename?: 'PodcastEpisodeResultSet';
  items: Array<Maybe<PodcastEpisode>>;
  pageIndex: Scalars['Int']['output'];
  pageSize: Scalars['Int']['output'];
  totalItems: Scalars['Int']['output'];
  totalPages: Scalars['Int']['output'];
};

export type PodcastFragment = {
  __typename?: 'PodcastFragment';
  content?: Maybe<Scalars['String']['output']>;
  host?: Maybe<Scalars['String']['output']>;
};

export type PodcastScript = {
  __typename?: 'PodcastScript';
  sections?: Maybe<Array<Maybe<PodcastSection>>>;
  title?: Maybe<Scalars['String']['output']>;
};

export type PodcastSection = {
  __typename?: 'PodcastSection';
  fragments?: Maybe<Array<Maybe<PodcastFragment>>>;
  title?: Maybe<Scalars['String']['output']>;
};

/** Query root */
export type Query = {
  __typename?: 'Query';
  /** Finds all podcast episodes */
  episodes: PodcastEpisodeResultSet;
  /** Finds all pending content submissions */
  pendingSubmissions: Array<Maybe<ContentSubmission>>;
  statistics: ApplicationStatistics;
  /** Finds all content submissions */
  submissions: ContentSubmissionResultSet;
};


/** Query root */
export type QueryEpisodesArgs = {
  pageIndex: Scalars['Int']['input'];
  pageSize: Scalars['Int']['input'];
};


/** Query root */
export type QuerySubmissionsArgs = {
  pageIndex: Scalars['Int']['input'];
  pageSize: Scalars['Int']['input'];
};

export enum SubmissionStatus {
  Processed = 'PROCESSED',
  Processing = 'PROCESSING',
  Submitted = 'SUBMITTED',
  Summarized = 'SUMMARIZED'
}

export type SubmitContentRequestInput = {
  instructions?: InputMaybe<Scalars['String']['input']>;
  url?: InputMaybe<Scalars['String']['input']>;
};

export type FetchEpisodesQueryQueryVariables = Exact<{
  pageIndex: Scalars['Int']['input'];
  pageSize: Scalars['Int']['input'];
}>;


export type FetchEpisodesQueryQuery = { __typename?: 'Query', episodes: { __typename?: 'PodcastEpisodeResultSet', totalPages: number, totalItems: number, items: Array<{ __typename?: 'PodcastEpisode', id: any, title: string } | null> } };

export type FetchPendingSubmissionsQueryQueryVariables = Exact<{ [key: string]: never; }>;


export type FetchPendingSubmissionsQueryQuery = { __typename?: 'Query', pendingSubmissions: Array<{ __typename?: 'ContentSubmission', id: any, title?: string | null, url: string } | null> };

export type FetchSubmissionsQueryQueryVariables = Exact<{
  pageIndex: Scalars['Int']['input'];
  pageSize: Scalars['Int']['input'];
}>;


export type FetchSubmissionsQueryQuery = { __typename?: 'Query', submissions: { __typename?: 'ContentSubmissionResultSet', totalPages: number, totalItems: number, items: Array<{ __typename?: 'ContentSubmission', id: any, title?: string | null, url: string } | null> } };

export type FetchStatisticsQueryQueryVariables = Exact<{ [key: string]: never; }>;


export type FetchStatisticsQueryQuery = { __typename?: 'Query', statistics: { __typename?: 'ApplicationStatistics', pendingSubmissions: any, totalEpisodes: any } };


export const FetchEpisodesQueryDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"query","name":{"kind":"Name","value":"fetchEpisodesQuery"},"variableDefinitions":[{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"pageIndex"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"Int"}}}},{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"pageSize"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"Int"}}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"episodes"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"pageIndex"},"value":{"kind":"Variable","name":{"kind":"Name","value":"pageIndex"}}},{"kind":"Argument","name":{"kind":"Name","value":"pageSize"},"value":{"kind":"Variable","name":{"kind":"Name","value":"pageSize"}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"totalPages"}},{"kind":"Field","name":{"kind":"Name","value":"totalItems"}},{"kind":"Field","name":{"kind":"Name","value":"items"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"title"}}]}}]}}]}}]} as unknown as DocumentNode<FetchEpisodesQueryQuery, FetchEpisodesQueryQueryVariables>;
export const FetchPendingSubmissionsQueryDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"query","name":{"kind":"Name","value":"fetchPendingSubmissionsQuery"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"pendingSubmissions"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"title"}},{"kind":"Field","name":{"kind":"Name","value":"url"}}]}}]}}]} as unknown as DocumentNode<FetchPendingSubmissionsQueryQuery, FetchPendingSubmissionsQueryQueryVariables>;
export const FetchSubmissionsQueryDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"query","name":{"kind":"Name","value":"fetchSubmissionsQuery"},"variableDefinitions":[{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"pageIndex"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"Int"}}}},{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"pageSize"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"Int"}}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"submissions"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"pageIndex"},"value":{"kind":"Variable","name":{"kind":"Name","value":"pageIndex"}}},{"kind":"Argument","name":{"kind":"Name","value":"pageSize"},"value":{"kind":"Variable","name":{"kind":"Name","value":"pageSize"}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"totalPages"}},{"kind":"Field","name":{"kind":"Name","value":"totalItems"}},{"kind":"Field","name":{"kind":"Name","value":"items"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"title"}},{"kind":"Field","name":{"kind":"Name","value":"url"}}]}}]}}]}}]} as unknown as DocumentNode<FetchSubmissionsQueryQuery, FetchSubmissionsQueryQueryVariables>;
export const FetchStatisticsQueryDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"query","name":{"kind":"Name","value":"fetchStatisticsQuery"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"statistics"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"pendingSubmissions"}},{"kind":"Field","name":{"kind":"Name","value":"totalEpisodes"}}]}}]}}]} as unknown as DocumentNode<FetchStatisticsQueryQuery, FetchStatisticsQueryQueryVariables>;