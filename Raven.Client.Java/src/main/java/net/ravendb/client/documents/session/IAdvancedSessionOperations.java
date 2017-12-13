package net.ravendb.client.documents.session;

import net.ravendb.client.documents.indexes.AbstractIndexCreationTask;
import net.ravendb.client.documents.session.operations.lazy.IEagerSessionOperations;
import net.ravendb.client.documents.session.operations.lazy.ILazySessionOperations;

import java.util.List;

public interface IAdvancedSessionOperations extends IAdvancedDocumentSessionOperations {

    /**
     *  Access the eager operations
     */
    //TBD IEagerSessionOperations eagerly();

    /**
     *  Access the lazy operations
     * @return
     */
    //TBD ILazySessionOperations lazily();

    //TBD IAttachmentsSessionOperations Attachments { get; }
    //TBD IRevisionsSessionOperations Revisions { get; }

    /**
     * Updates entity with latest changes from server
     */
    <T> void refresh(T entity);

    /**
     * Query the specified index using provided raw query
     */
    <T>IRawDocumentQuery<T> rawQuery(Class<T> clazz, String query);

    /**
     * Check if document exists
     */
    boolean exists(String id);

    /**
     * Loads multiple entities that contain common prefix.
     * @param clazz entity class
     * @param idPrefix prefix for which documents should be returned e.g. "products/"
     * @param <T> entity class
     */
    <T> T[] loadStartingWith(Class<T> clazz, String idPrefix);

    /**
     * Loads multiple entities that contain common prefix.
     * @param clazz entity class
     * @param idPrefix prefix for which documents should be returned e.g. "products/"
     * @param matches  pipe ('|') separated values for which document IDs (after 'idPrefix') should be matched ('?' any single character, '*' any characters)
     * @param <T> entity class
     */
    <T> T[] loadStartingWith(Class<T> clazz, String idPrefix, String matches);

    /**
     * Loads multiple entities that contain common prefix.
     * @param clazz entity class
     * @param idPrefix prefix for which documents should be returned e.g. "products/"
     * @param matches  pipe ('|') separated values for which document IDs (after 'idPrefix') should be matched ('?' any single character, '*' any characters)
     * @param start number of documents that should be skipped. By default: 0.
     * @param <T> entity class
     */
    <T> T[] loadStartingWith(Class<T> clazz, String idPrefix, String matches, int start);

    /**
     * Loads multiple entities that contain common prefix.
     * @param clazz entity class
     * @param idPrefix prefix for which documents should be returned e.g. "products/"
     * @param matches  pipe ('|') separated values for which document IDs (after 'idPrefix') should be matched ('?' any single character, '*' any characters)
     * @param start number of documents that should be skipped. By default: 0.
     * @param pageSize maximum number of documents that will be retrieved. By default: 25.
     * @param <T> entity class
     */
    <T> T[] loadStartingWith(Class<T> clazz, String idPrefix, String matches, int start, int pageSize);

    /**
     * Loads multiple entities that contain common prefix.
     * @param clazz entity class
     * @param idPrefix prefix for which documents should be returned e.g. "products/"
     * @param matches  pipe ('|') separated values for which document IDs (after 'idPrefix') should be matched ('?' any single character, '*' any characters)
     * @param start number of documents that should be skipped. By default: 0.
     * @param pageSize maximum number of documents that will be retrieved. By default: 25.
     * @param exclude pipe ('|') separated values for which document IDs (after 'idPrefix') should not be matched ('?' any single character, '*' any characters)
     * @param <T> entity class
     */
    <T> T[] loadStartingWith(Class<T> clazz, String idPrefix, String matches, int start, int pageSize, String exclude);

    /**
     * Loads multiple entities that contain common prefix.
     * @param clazz entity class
     * @param idPrefix prefix for which documents should be returned e.g. "products/"
     * @param matches  pipe ('|') separated values for which document IDs (after 'idPrefix') should be matched ('?' any single character, '*' any characters)
     * @param start number of documents that should be skipped. By default: 0.
     * @param pageSize maximum number of documents that will be retrieved. By default: 25.
     * @param exclude pipe ('|') separated values for which document IDs (after 'idPrefix') should not be matched ('?' any single character, '*' any characters)
     * @param startAfter skip document fetching until given ID is found and return documents after that ID (default: null)
     * @param <T> entity class
     */
    <T> T[] loadStartingWith(Class<T> clazz, String idPrefix, String matches, int start, int pageSize, String exclude, String startAfter);

    //TBD void LoadStartingWithIntoStream(string idPrefix, Stream output, string matches = null, int start = 0, int pageSize = 25, string exclude = null, string startAfter = null);
    //TBD void LoadIntoStream(IEnumerable<string> ids, Stream output);
    //TBD List<T> MoreLikeThis<T, TIndexCreator>(string documentId) where TIndexCreator : AbstractIndexCreationTask, new();
    //TBD List<T> MoreLikeThis<T, TIndexCreator>(MoreLikeThisQuery query) where TIndexCreator : AbstractIndexCreationTask, new();
    //TBD List<T> MoreLikeThis<T>(string index, string documentId);
    //TBD List<T> MoreLikeThis<T>(MoreLikeThisQuery query);
    //TBD patch API void Increment<T, U>(T entity, Expression<Func<T, U>> path, U valToAdd);
    //TBD patch API void Increment<T, U>(string id, Expression<Func<T, U>> path, U valToAdd);
    //TBD patch API void Patch<T, U>(string id, Expression<Func<T, U>> path, U value);
    //TBD patch API void Patch<T, U>(T entity, Expression<Func<T, U>> path, U value);
    //TBD patch API void Patch<T, U>(T entity, Expression<Func<T, IEnumerable<U>>> path, Expression<Func<JavaScriptArray<U>, object>> arrayAdder);
    //TBD patch API void Patch<T, U>(string id, Expression<Func<T, IEnumerable<U>>> path, Expression<Func<JavaScriptArray<U>, object>> arrayAdder);

    <T, TIndex extends AbstractIndexCreationTask> IDocumentQuery<T> documentQuery(Class<T> clazz, Class<TIndex> indexClazz);

    /**
     * Query the specified index using Lucene syntax
     * @param clazz The result of the query
     * @param indexName Name of the index (mutually exclusive with collectionName)
     * @param collectionName Name of the collection (mutually exclusive with indexName)
     * @param isMapReduce Whether we are querying a map/reduce index (modify how we treat identifier properties)
     */
    <T> IDocumentQuery<T> documentQuery(Class<T> clazz, String indexName, String collectionName, boolean isMapReduce);


    // TBD stream IEnumerator<StreamResult<T>> Stream<T>(IQueryable<T> query);
    // TBD stream IEnumerator<StreamResult<T>> Stream<T>(IQueryable<T> query, out StreamQueryStatistics streamQueryStats);
    // TBD stream IEnumerator<StreamResult<T>> Stream<T>(IDocumentQuery<T> query);
    // TBD stream IEnumerator<StreamResult<T>> Stream<T>(IRawDocumentQuery<T> query);
    // TBD stream IEnumerator<StreamResult<T>> Stream<T>(IRawDocumentQuery<T> query, out StreamQueryStatistics streamQueryStats);
    // TBD stream IEnumerator<StreamResult<T>> Stream<T>(IDocumentQuery<T> query, out StreamQueryStatistics streamQueryStats);
    // TBD stream IEnumerator<StreamResult<T>> Stream<T>(string startsWith, string matches = null, int start = 0, int pageSize = int.MaxValue, string startAfter = null);
    // TBD stream void StreamInto<T>(IDocumentQuery<T> query, Stream output);
    // TBD stream void StreamInto<T>(IRawDocumentQuery<T> query, Stream output);

}
