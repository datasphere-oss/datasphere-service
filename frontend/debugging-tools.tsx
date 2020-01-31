import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"modal modal3 dku-modal\">
    +             <div dku-modal-header-with-totem=\"\" modal-title=\"Secret tools\" modal-totem=\"icon-caret-up\">
    +                 <div className=\"modal-body\">
    +                     <button
    +                         onClick={() => {
    +                             killBackend();
    +                         }}
    +                         className=\"btn btn-danger\">
    +                         杀掉进程
    +                     </button>
    +                     <button
    +                         onClick={() => {
    +                             getBackendStacks();
    +                         }}
    +                         className=\"btn btn-success\">
    +                         打印后台信息
    +                     </button>
    +                     <button
    +                         onClick={() => {
    +                             restartAllHTMLBackends();
    +                         }}
    +                         className=\"btn btn-danger margin-td-10\">
    +                         重启 HTML 应用后台
    +                     </button>
    +                     <button
    +                         onClick={() => {
    +                             reloadCustomRecipes();
    +                         }}
    +                         className=\"btn btn-success\">
    +                         重载自定义组件{' '}
    +                     </button>
    +                     <button
    +                         onClick={() => {
    +                             runScenarioTriggers();
    +                         }}
    +                         className=\"btn btn-success\">
    +                         运行场景触发器
    +                     </button>
    +                     <button
    +                         onClick={() => {
    +                             $state.go('blackhole');
    +                         }}
    +                         className=\"btn btn-default\">
    +                         转到黑洞
    +                     </button>
    +                     <br />
    +                     <button
    +                         className=\"btn btn-success margin-td-10\"
    +                         onClick={() => {
    +                             sendOfflineQueues();
    +                         }}>
    +                         发送用户离线队列邮件
    +                     </button>
    +                     <button
    +                         className=\"btn btn-success\"
    +                         onClick={() => {
    +                             sendDigests();
    +                         }}>
    +                         发送每日邮件
    +                     </button>
    +                     <br />
    +                     假活动:{' '}
    +                     <select
    +                         dku-bs-select=\"\"
    +                         onChange={() => {
    +                             insertFakeFuture();
    +                         }}
    +                         value={uiState.fakeFutureType}
    +                         ng-options=\"f as f.name for f in fakeFutureTypes\"
    +                     />
    +                     <pre className=\"debug\" style=\"max-width: none; margin-top: 20px\">
    +                         {retdata}
    +                     </pre>
    +                 </div>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;