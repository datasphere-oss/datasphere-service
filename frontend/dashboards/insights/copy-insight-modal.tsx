import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div
    +             className=\"modal modal3 new-insight-modal dku-modal\"
    +             ng-controller=\"CopyInsightModalController\"
    +             auto-size=\"false\">
    +             <div
    +                 dku-modal-header=\"\"
    +                 modal-class=\"noflex has-border\"
    +                 modal-title={withSimpleTiles ? '拷贝' : '拷贝洞察'}>
    +                 <form name=\"copyInsightForm\" className=\"dkuform-modal-horizontal dkuform-modal-wrapper\">
    +                     {insight.type ? (
    +                         <div className=\"modal-body\">
    +                             <div block-api-error=\"\">
    +                                 <div className=\"control-group\">
    +                                     <label className=\"control-label\" htmlFor=\"copyInsightNameInput\">
    +                                         拷贝探索名称
    +                                     </label>
    +                                     <div className=\"controls\">
    +                                         <input
    +                                             type=\"text\"
    +                                             value={insight.newName}
    +                                             placeholder={insight.name}
    +                                             id=\"copyInsightNameInput\"
    +                                         />
    +                                     </div>
    +                                 </div>
    +                             </div>
    + 
    +                             <div className=\"modal-footer modal-footer-std-buttons\">
    +                                 <button
    +                                     type=\"button\"
    +                                     className=\"btn btn-default\"
    +                                     onClick={() => {
    +                                         dismiss();
    +                                     }}>
    +                                     取消
    +                                 </button>
    +                                 <button
    +                                     type=\"submit\"
    +                                     disabled={!copyInsightForm.$valid}
    +                                     className=\"btn btn-primary\"
    +                                     onClick={() => {
    +                                         copy();
    +                                     }}>
    +                                     拷贝探索
    +                                 </button>
    +                             </div>
    +                         </div>
    +                     ) : null}
    +                 </form>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;