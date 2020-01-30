import * as React from 'react';
     
export interface TestComponentProps {
 [key: string]: any;
}

const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
 return (
     <div ng-controller=\"ColumnEditUDMController\" className=\"modal modal3 edit-udm-modal\" auto-size=\"false\">
         <div className=\"catalog-tooltips\" />
         <div dku-modal-header-with-totem=\"\" modal-title={`Meaning for \"${columnName}\"`} modal-totem=\"icon-tags\">
             <div block-api-error=\"\">
                 {/* Search view */}
                 {state == 'search' ? (
                     <div
                         selected-index=\"saveSet(item._source.id)\"
                         value={results.hits.hits}
                         deselect=\"true\"
                         in-modal=\"true\">
                         <div className=\"modal-body udm-search vertical-flex\">
                             <div className=\"summary noflex\">
                                 <a
                                     className=\"btn btn-default pull-right\"
                                     onClick={() => {
                                         dismiss();
                                     }}
                                     href=\"/catalog/meanings/\"
                                     style=\"margin-left: 5px;\">
                                     <i className=\"icon-pencil\"> 编辑目录 </i>
                                 </a>
                                 <i className=\"icon-pencil\">
                                     <button
                                         type=\"button\"
                                         className=\"btn btn-default pull-right\"
                                         onClick={() => {
                                             createNew();
                                         }}>
                                         <i plus-icon=\"\"> 创建含义</i>
                                     </button>
                                     <i plus-icon=\"\">
                                         <div className=\"std-list-search-box\">
                                             <span className=\"add-on\">
                                                 {' '}
                                                 <i className=\"icon-dku-search\" />
                                             </span>
                                             <input
                                                 type=\"search\"
                                                 value={query.queryString}
                                                 onKeyDown={(event: React.SyntheticEvent<HTMLElement>) => {
                                                     blurOnEnter(event);
                                                 }}
                                                 placeholder=\"搜索含义\"
                                             />
                                         </div>
                                     </i>
                                 </i>
                             </div>
                             <i className=\"icon-pencil\">
                                 <i plus-icon=\"\">
                                     <div className=\"flex\">
                                         <div className=\"horizontal-flex row-fluid fh\">
                                             <div className=\"span7 flex fh\">
                                                 {results.hits.hits.length === 0 ? (
                                                     <div className=\"alert alert-info\" style=\"border-radius: 0\">
                                                         <h4>
                                                             无 {query.queryString !== 'saved' ? ' matching' : ''}{' '}
                                                             含义
                                                         </h4>
                                                         <p style=\"text-align: right;\">
                                                             <a
                                                                 className=\"btn btn-default\"
                                                                 onClick={() => {
                                                                     createNew();
                                                                 }}>
                                                                 <i plus-icon=\"\"> 创建含义</i>
                                                             </a>
                                                             <i plus-icon=\"\">
                                                                 {query.queryString !== '' ? (
                                                                     <a
                                                                         className=\"btn btn-default\"
                                                                         onClick={() => {
                                                                             query.queryString = '';
                                                                         }}>
                                                                         清空搜索
                                                                     </a>
                                                                 ) : null}
                                                             </i>
                                                         </p>
                                                         <i plus-icon=\"\" />
                                                     </div>
                                                 ) : null}
                                                 <i plus-icon=\"\">
                                                     <ul>
                                                         {results.hits.hits.map((meaning, index: number) => {
                                                             return (
                                                                 <li
                                                                     key={`item-${index}`}
                                                                     className=\"{active: $index === selected.index &amp;&amp; selected.item}\"
                                                                     onClick={() => {
                                                                         selectIndex($index);
                                                                     }}
                                                                     scroll-to-me={
                                                                         $index === selected.index ? 'true' : 'false'
                                                                     }
                                                                     className={index % 2 ? undefined : 'even'}>
                                                                     <div className=\"horizontal-flex\">
                                                                         <span className=\"flex mx-textellipsis\">
                                                                             {meaning._source.prefix ? (
                                                                                 <span className=\"prefix\">
                                                                                     <span
                                                                                         className=\"highlight\"
                                                                                         dangerouslySetInnerHTML={{
                                                                                             __html:
                                                                                                 'meaning.highlight.prefix[0] || meaning._source.prefix'
                                                                                         }}
                                                                                     />_
                                                                                 </span>
                                                                             ) : null}
                                                                             <span
                                                                                 className=\"label highlight\"
                                                                                 dangerouslySetInnerHTML={{
                                                                                     __html:
                                                                                         'meaning.highlight.label[0] || meaning._source.label'
                                                                                 }}
                                                                             />
                                                                         </span>

                                                                         <span className=\"noflex type highlight\">
                                                                             {lowercase(
                                                                                 udmTypes[meaning._source.udm_type]
                                                                             )}
                                                                         </span>
                                                                     </div>
                                                                 </li>
                                                             );
                                                         })}
                                                     </ul>
                                                 </i>
                                             </div>
                                             <i plus-icon=\"\">
                                                 <div
                                                     className=\"span5 object-right-column-summary\"
                                                     ng-controller=\"ColumnEditUDMRefreshController\">
                                                     {!!selected.item ? (
                                                         <NgInclude src=\"'/templates/catalog/meanings-rightcolumn.html'\" />
                                                     ) : null}
                                                 </div>
                                             </i>
                                         </div>
                                         <i plus-icon=\"\" />
                                     </div>
                                     <i plus-icon=\"\" />
                                 </i>
                             </i>
                         </div>
                         <i className=\"icon-pencil\">
                             <i plus-icon=\"\">
                                 <i plus-icon=\"\">
                                     <div className=\"modal-footer modal-footer-std-buttons has-border\">
                                         <div className=\"pull-right\">
                                             <button
                                                 type=\"button\"
                                                 className=\"btn btn-default\"
                                                 onClick={() => {
                                                     dismiss();
                                                 }}>
                                                 取消
                                             </button>
                                             <button
                                                 type=\"submit\"
                                                 className=\"btn btn-primary\"
                                                 onClick={() => {
                                                     saveSet(selected.item._source.id);
                                                 }}
                                                 disabled={!selected.item}>
                                                 选择
                                             </button>
                                         </div>
                                     </div>
                                 </i>
                             </i>
                         </i>
                     </div>
                 ) : null}
                 <i className=\"icon-pencil\">
                     <i plus-icon=\"\">
                         <i plus-icon=\"\">
                             {state == 'new' ? (
                                 <div>
                                     <div className=\"modal-body\">
                                         <NgInclude src=\"'/templates/meanings/udm-definition-form.html'\" />
                                     </div>

                                     <div className=\"modal-footer modal-footer-std-buttons has-border\">
                                         <div className=\"pull-right\">
                                             <button
                                                 type=\"button\"
                                                 className=\"btn btn-default\"
                                                 onClick={() => {
                                                     dismiss();
                                                 }}>
                                                 取消
                                             </button>
                                             <span
                                                 style=\"display: inline-block;\"
                                                 onMouseEnter={() => {
                                                     form.mouseOverSave = true;
                                                 }}
                                                 onMouseLeave={() => {
                                                     form.mouseOverSave = false;
                                                 }}>
                                                 <button
                                                     type=\"submit\"
                                                     className=\"btn btn-primary\"
                                                     onClick={() => {
                                                         saveNew();
                                                     }}
                                                     disabled={saving || form.$invalid}>
                                                     创建和设置
                                                 </button>
                                             </span>
                                         </div>
                                     </div>
                                 </div>
                             ) : null}
                         </i>
                     </i>
                 </i>
             </div>
         </div>
     </div>
 );
};

export default TestComponent;