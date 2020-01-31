import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"modal modal3 dku-modal\">
    +             <div dku-modal-header-with-totem=\"\" modal-title=\"A conflict occurred\" modal-totem=\"icon-warning-sign\">
    +                 <div className=\"modal-body\">
    +                     <p>{conflictResult.message}</p>
    +                     <p>
    +                         {conflictResult.delta > 1 ? (
    +                             <span>
    +                                 它已经被修改 {conflictResult.delta} 次, 当你在上面工作时. 最后的修改已经被完成, 通过{' '}
    +                                 <b>{conflictResult.lastModifiedBy.displayName}</b>
    +                                 {friendlyTimeDelta(conflictResult.lastModifiedOn)}.
    +                             </span>
    +                         ) : null}
    +                         {conflictResult.delta == 1 ? (
    +                             <span>
    +                                 它已经被修改 {friendlyTimeDelta(conflictResult.lastModifiedOn)} 通过
    +                                 <b>{conflictResult.lastModifiedBy.displayName}</b>.
    +                             </span>
    +                         ) : null}
    +                     </p>
    +                 </div>
    +                 <div
    +                     className=\"modal-footer modal-footer-std-buttons\"
    +                     global-keydown=\"{'enter':'confirm();', 'esc': 'cancel();' }\">
    +                     <div className=\"pull-right\">
    +                         <button
    +                             className=\"btn btn-primary\"
    +                             onClick={() => {
    +                                 erase();
    +                             }}>
    +                             <i className=\"icon-hdd\" />
    +                             保存
    +                         </button>
    + 
    +                         <button
    +                             className=\"btn btn-danger\"
    +                             onClick={() => {
    +                                 forget();
    +                             }}>
    +                             <i className=\"icon-retweet\" />
    +                             放弃你的更新
    +                         </button>
    + 
    +                         <button
    +                             className=\"btn btn-default\"
    +                             onClick={() => {
    +                                 cancel();
    +                             }}>
    +                             <i className=\"icon-time\" />
    +                             稍后决定
    +                         </button>
    +                     </div>
    +                 </div>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;