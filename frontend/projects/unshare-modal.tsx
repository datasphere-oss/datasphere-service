import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"modal modal3 new-dashboard-modal dku-modal expose-object-modal\" auto-size=\"false\">
    +             <div
    +                 dku-modal-header-with-totem=\"\"
    +                 modal-class=\"has-border\"
    +                 modal-title=\"Stop sharing\"
    +                 modal-totem=\"icon-dku-share\">
    +                 <form name=\"copyDashboardForm\" className=\"dkuform-modal-vertical dkuform-modal-wrapper\">
    +                     <div className=\"modal-body\">
    +                         <div block-api-error=\"\">
    +                             你确定想要停止共享吗
    +                             {selectedObjects.length < 2 ? (
    +                                 <span>this {niceTaggableType(selectedObjectsType)}?</span>
    +                             ) : null}
    +                             {selectedObjects.length >= 2 ? (
    +                                 <span>these {niceTaggableType(selectedObjectsType)}s?</span>
    +                             ) : null}
    +                             <p>要在此项目中再次公开它们，请转到其原始项目设置.</p>
    +                         </div>
    + 
    +                         <div className=\"modal-footer modal-footer-std-buttons full-width\">
    +                             <button
    +                                 type=\"submit\"
    +                                 className=\"btn btn-primary\"
    +                                 onClick={() => {
    +                                     ok();
    +                                 }}>
    +                                 好的
    +                             </button>
    +                         </div>
    +                     </div>
    +                 </form>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;