import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div
    +             className=\"modal modal3 code-edit-schema-box\"
    +             code-recipe-schema-editing=\"\"
    +             on-smart-change=\"setSchemaUserModified()\"
    +             value={dataset.schema.columns}
    +             ng-scope=\"{maxSchemaItems = 20; hideAdd: true}\"
    +             ng-init=\"hideAdd = true\">
    +             <div dku-modal-header=\"\" modal-title={`Schema \"${dataset.name}\"`} modal-class=\"has-border\">
    +                 <div className=\"modal-body\">
    +                     <div
    +                         schema-consistency-status=\"\"
    +                         current-schema=\"dataset.schema\"
    +                         consistency=\"consistency\"
    +                         overwrite-schema=\"overwriteSchema\"
    +                         clear-managed-dataset=\"clearDataset\"
    +                         check-consistency=\"checkConsistency\"
    +                         discard-consistency-error=\"discardConsistencyError\"
    +                         managed=\"dataset.managed\"
    +                         className=\"noflex\">
    +                         <div style=\"height: 350px; position: relative;\">
    +                             <NgInclude src=\"'/templates/datasets/schema-edition.html'\" />
    +                         </div>
    +                     </div>
    + 
    +                     <div className=\"modal-footer has-border\" style=\"padding-left: 15px;\">
    +                         <div className=\"actions\" style=\"float: left;\">
    +                             <button
    +                                 type=\"submit\"
    +                                 className=\"btn btn-default\"
    +                                 onClick={() => {
    +                                     addColumn();
    +                                 }}>
    +                                 <span
    +                                     style=\"font-size:1.3em; vertical-align: top;font-size:1.3em; vertical-align: top\"
    +                                     plus-icon=\"\">
    +                                     +
    +                                 </span>
    +                                 添加列字段
    +                             </button>
    +                         </div>
    + 
    +                         <div className=\"text-right actions  modal-footer-std-buttons\">
    +                             <button
    +                                 type=\"button\"
    +                                 className=\"btn btn-default\"
    +                                 onClick={() => {
    +                                     dismiss();
    +                                 }}>
    +                                 取消
    +                             </button>
    +                             <button
    +                                 type=\"submit\"
    +                                 className=\"btn btn-primary\"
    +                                 onClick={() => {
    +                                     saveSchema();
    +                                 }}>
    +                                 保存
    +                             </button>
    +                         </div>
    +                     </div>
    +                 </div>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;