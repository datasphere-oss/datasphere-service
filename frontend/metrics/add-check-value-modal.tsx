import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"modal modal3 metrics-run-settings\">
    +             <div dku-modal-header=\"\" modal-title=\"Insert a check value\" modal-class=\"has-border\">
    +                 <form className=\"dkuform-modal-horizontal dkuform-modal-wrapper\">
    +                     <div className=\"modal-body\">
    +                         <fieldset>
    +                             <div className=\"control-group\">
    +                                 <label className=\"control-label\">名称</label>
    +                                 <div className=\"controls\">
    +                                     <input type=\"text\" value={newCheck.name} />
    +                                 </div>
    +                             </div>
    +                         </fieldset>
    +                         <fieldset>
    +                             <div className=\"control-group\">
    +                                 <label className=\"control-label\">值</label>
    +                                 <div className=\"controls\">
    +                                     <select dku-bs-select=\"\" value={newCheck.value}>
    +                                         <option value=\"OK\">OK</option>
    +                                         <option value=\"ERROR\">错误</option>
    +                                         <option value=\"WARNING\">警告</option>
    +                                         <option value=\"EMPTY\">空</option>
    +                                     </select>
    +                                 </div>
    +                             </div>
    +                         </fieldset>
    +                         <fieldset>
    +                             <div className=\"control-group\">
    +                                 <label className=\"control-label\">消息</label>
    +                                 <div className=\"controls\">
    +                                     <input type=\"text\" value={newCheck.message} />
    +                                 </div>
    +                             </div>
    +                         </fieldset>
    +                     </div>
    + 
    +                     <div className=\"modal-footer modal-footer-std-buttons\">
    +                         <button
    +                             className=\"btn btn-default\"
    +                             onClick={() => {
    +                                 dismiss();
    +                             }}>
    +                             取消
    +                         </button>
    +                         <button
    +                             className=\"btn btn-primary\"
    +                             onClick={() => {
    +                                 addCheckPoint(newCheck);
    +                             }}
    +                             disabled={!newCheck.name || !newCheck.value}>
    +                             Add
    +                         </button>
    +                     </div>
    +                 </form>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;