import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"modal modal3\">
    +             <div dku-modal-header=\"\" modal-title=\"Convert to plugin recipe\" modal-class=\"has-border\">
    +                 <form name=\"conversionForm\" className=\"dkuform-modal-horizontal dkuform-modal-wrapper \">
    +                     <div className=\"modal-body\">
    +                         <p>此动作将覆盖任意现有的自定义源 '{convert.targetPluginDesc.meta.label}'.</p>
    +                     </div>
    +                     <div className=\"modal-footer modal-footer-std-buttons\">
    +                         <button
    +                             type=\"button\"
    +                             className=\"btn btn-default\"
    +                             onClick={() => {
    +                                 dismiss();
    +                             }}>
    +                             取消
    +                         </button>
    +                         <button
    +                             type=\"submit\"
    +                             className=\"btn btn-primary\"
    +                             onClick={() => {
    +                                 go();
    +                             }}
    +                             disabled={conversionForm.$invalid}>
    +                             确定
    +                         </button>
    +                     </div>
    +                 </form>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;