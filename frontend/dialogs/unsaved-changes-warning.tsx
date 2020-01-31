import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"modal modal3 move-copy-modal dku-modal\" auto-size=\"false\">
    +             <div
    +                 dku-modal-header-with-totem=\"\"
    +                 modal-class=\"noflex\"
    +                 modal-title=\"未保存更新\"
    +                 modal-totem=\"icon-warning-sign\">
    +                 <div className=\"modal-body\">
    +                     <div block-api-error=\"\">{msg || '你有未保存的更新.'}</div>
    + 
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
    +                             type=\"button\"
    +                             className=\"btn btn-danger\"
    +                             onClick={() => {
    +                                 continueWithoutSaving();
    +                             }}>
    +                             不保存继续
    +                         </button>
    +                         <button
    +                             type=\"button\"
    +                             className=\"btn btn-primary\"
    +                             onClick={() => {
    +                                 saveAndContinue();
    +                             }}>
    +                             保存和继续
    +                         </button>
    +                     </div>
    +                 </div>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;