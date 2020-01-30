import * as React from 'react';
     
export interface TestComponentProps {
 [key: string]: any;
}
     
const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
 return (
     <div
         ng-controller=\"EditUDMController\"
         className=\"modal modal3 edit-udm-modal\"
         tab-model=\"uiState.activeTab\"
         auto-size=\"false\">
         <DkuModalHeaderWithTotem modal-totem=\"icon-dku-meanings\">
             <DkuModalTitle>{creation ? '创建维度' : 'Edit meaning \"'  udm.id  '\"'}</DkuModalTitle>
             <DkuModalTabs>
                 <li className=\"tab\" tab-active=\"definition\">
                     <a tab-set=\"definition\">定义</a>
                 </li>
                 <li className=\"tab\" tab-active=\"columns\">
                     <a tab-set=\"columns\">列</a>
                 </li>
             </DkuModalTabs>
         </DkuModalHeaderWithTotem>

         <div block-api-error=\"\" />

         <div className=\"modal-body\" ng-switch=\"uiState.activeTab\" style=\"height: 700px;\" watch-scroll=\"\">
             <div ng-switch-when=\"definition\" className=\"h100\">
                 <NgInclude src=\"'/templates/meanings/udm-definition-form.html'\" className=\"h100\" />
             </div>

             <div ng-switch-when=\"columns\" className=\"h100\">
                 <table style=\"background: transparent\" className=\"vertical-flex h100\">
                     <thead className=\"noflex\">
                         <tr>
                             <th>
                                 <input
                                     type=\"checkbox\"
                                     value={$parent.toggleAll}
                                     dku-indeterminate=\"indeterminate()\"
                                 />
                             </th>
                             <th>
                                 <div className=\"std-list-search-box\" style=\"margin-left: 1px\">
                                     <span className=\"add-on\">
                                         {' '}
                                         <i className=\"icon-dku-search\" />
                                     </span>
                                     <input type=\"search\" value={$parent.query} placeholder=\"搜索列\" />
                                 </div>
                             </th>

                             <th>数据集</th>

                             <th>工程</th>

                             <th>含义</th>
                         </tr>
                     </thead>

                     <tbody className=\"flex\">
                         {results.hits.hits.map((column, index: number) => {
                             return (
                                 <tr key={`item-${index}`} className={index % 2 ? undefined : 'even'} full-click=\"\">
                                     <td>
                                         {column._source.meaning === udm.id ? (
                                             <input
                                                 type=\"checkbox\"
                                                 main-click=\"\"
                                                 value={checkboxes[column._id]}
                                                 checked={true}
                                                 ng-true-value=\"'no_change'\"
                                                 ng-false-value=\"'remove'\"
                                                 onChange={() => {
                                                     updateCounts();
                                                 }}
                                             />
                                         ) : null}
                                         {column._source.meaning !== udm.id ? (
                                             <input
                                                 type=\"checkbox\"
                                                 main-click=\"\"
                                                 value={checkboxes[column._id]}
                                                 ng-true-value=\"'add'\"
                                                 ng-false-value=\"'no_change'\"
                                                 onChange={() => {
                                                     updateCounts();
                                                 }}
                                             />
                                         ) : null}
                                     </td>

                                     <td
                                         className=\"highlight\"
                                         dangerouslySetInnerHTML={{
                                             __html: 'column.highlight.name[0] || column._source.name'
                                         }}
                                     />

                                     <td>{column._source.dataset}</td>

                                     <td>{column._source.projectName}</td>

                                     <td className=\"{disabled: checkboxes[column._id] &amp;&amp; checkboxes[column._id] != 'no_change'}\">
                                         {column._source.meaning ? (
                                             <em>
                                                 <small style=\"color: #999\">
                                                     <i className=\"icon-lock\" />
                                                 </small>
                                                 {column._source.meaning}
                                             </em>
                                         ) : null}
                                         {!column._source.meaning ? <span>自动</span> : null}
                                     </td>
                                 </tr>
                             );
                         })}
                     </tbody>
                 </table>
             </div>
         </div>

         <div className=\"modal-footer modal-footer-std-buttons has-border\">
             <div className=\"pull-left\">
                 <p style=\"font-size:12px; padding-left: 10px; margin: 0;\">
                     {changeCounts.add ? <span>分配给 {changeCounts.add} 列</span> : null}
                     {changeCounts.remove ? <span>从 {changeCounts.remove} 列删除</span> : null}
                 </p>
             </div>

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
                             save();
                         }}
                         disabled={saving || form.$invalid}>
                         保存
                     </button>
                 </span>
             </div>
         </div>
     </div>
 );
};

export default TestComponent;