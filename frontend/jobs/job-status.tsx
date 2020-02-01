import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div
    +             job-status-base=\"\"
    +             subscribe-notification=\"job-state-change\"
    +             subscribe-callback=\"refreshStatus\"
    +             className=\"job-status\">
    +             <div className=\"horizontal-flex row-fluid object-nav\">
    +                 <div className=\"flex oh\" std-object-breadcrumb=\"\">
    +                     <div className=\"noflex\">
    +                         <a
    +                             className=\"{'enabled': topNav.tab == 'summary'}\"
    +                             onClick={() => {
    +                                 topNav.tab = 'summary';
    +                             }}
    +                             fw500-width=\"\">
    +                             概要
    +                         </a>
    +                         <a
    +                             className=\"{'enabled': topNav.tab == 'activities'}\"
    +                             onClick={() => {
    +                                 topNav.tab = 'activities';
    +                             }}
    +                             fw500-width=\"\">
    +                             活动
    +                         </a>
    +                         <a
    +                             className=\"{'enabled': topNav.tab == 'graph'}\"
    +                             onClick={() => {
    +                                 topNav.tab = 'graph';
    +                             }}
    +                             fw500-width=\"\">
    +                             图
    +                         </a>
    + 
    +                         <div className=\"otherLinks\">
    +                             <div className=\"dropdown\" style=\"display:inline-block;\">
    +                                 <button href=\"\" data-toggle=\"dropdown\" className=\"btn btn-action btn-small\">
    +                                     操作 <i className=\"icon-caret-down\" />
    +                                 </button>
    +                                 <ul className=\"dropdown-menu\">
    +                                     {jobStatus.baseStatus.state == 'FAILED' ? (
    +                                         <li>
    +                                             <a
    +                                                 onClick={() => {
    +                                                     retryJob();
    +                                                 }}>
    +                                                 {' '}
    +                                                 <i className=\"icon-refresh\">&nbsp;重试此任务</i>
    +                                             </a>
    +                                             <i className=\"icon-refresh\" />
    +                                         </li>
    +                                     ) : null}
    +                                     <i className=\"icon-refresh\">
    +                                         {/* Change wording when it did not fail */}
    +                                         {jobStatus.baseStatus.state != 'FAILED' ? (
    +                                             <li>
    +                                                 <a
    +                                                     onClick={() => {
    +                                                         retryJob();
    +                                                     }}>
    +                                                     <i className=\"icon-refresh\">&nbsp;重新运行此任务</i>
    +                                                 </a>
    +                                                 <i className=\"icon-refresh\" />
    +                                             </li>
    +                                         ) : null}
    +                                         <i className=\"icon-refresh\">
    +                                             <li className=\"divider\" />
    +                                             <li>
    +                                                 <a
    +                                                     target=\"_new\"
    +                                                     href={`/dip/api/flow/jobs/cat-job-log?projectKey=${
    +                                                         $stateParams.projectKey
    +                                                     }&jobId=${jobStatus.baseStatus.def.id}`}>
    +                                                     <i className=\"icon-file-text-alt\" />
    +                                                     &nbsp;查看全部任务日志
    +                                                 </a>
    +                                             </li>
    +                                             <li>
    +                                                 <a
    +                                                     onClick={() => {
    +                                                         downloadJobDiagnosis();
    +                                                     }}>
    +                                                     <i className=\"icon-download-alt\" />
    +                                                     &nbsp;下载任务诊断
    +                                                 </a>
    +                                             </li>
    +                                         </i>
    +                                     </i>
    +                                 </ul>
    +                                 <i className=\"icon-refresh\">
    +                                     <i className=\"icon-refresh\" />
    +                                 </i>
    +                             </div>
    +                             <i className=\"icon-refresh\">
    +                                 <i className=\"icon-refresh\" />
    +                             </i>
    +                         </div>
    +                         <i className=\"icon-refresh\">
    +                             <i className=\"icon-refresh\" />
    +                         </i>
    +                     </div>
    +                     <i className=\"icon-refresh\">
    +                         <i className=\"icon-refresh\" />
    +                     </i>
    +                 </div>
    +                 <i className=\"icon-refresh\">
    +                     <i className=\"icon-refresh\">
    +                         <div className=\"dss-page\">
    +                             <div block-api-error=\"\">
    +                                 {jobState == 'NOT_STARTED' ? (
    +                                     <div className=\"header small-lr-padding page-top-padding\">
    +                                         <h4>任务即将开始, 请等待</h4>
    +                                     </div>
    +                                 ) : null}
    + 
    +                                 {jobState == 'COMPUTING_DEPS' ? (
    +                                     <div className=\"header small-lr-padding page-top-padding\">
    +                                         <div className=\"actions btn-group pull-right\">
    +                                             <button
    +                                                 className=\"btn-small btn btn-success\"
    +                                                 onClick={() => {
    +                                                     refreshStatus();
    +                                                 }}>
    +                                                 <i className=\"icon-refresh\">&nbsp;刷新</i>
    +                                             </button>
    +                                             <i className=\"icon-refresh\">
    +                                                 <button
    +                                                     className=\"btn-small btn btn-danger\"
    +                                                     onClick={() => {
    +                                                         abortJob(jobDef.id);
    +                                                     }}>
    +                                                     <i className=\"icon-remove-sign\">&nbsp;放弃</i>
    +                                                 </button>
    +                                                 <i className=\"icon-remove-sign\" />
    +                                             </i>
    +                                         </div>
    +                                         <i className=\"icon-refresh\">
    +                                             <i className=\"icon-remove-sign\">
    +                                                 <h4 style=\"margin-top: 100px; text-align: center; font-weight: 300; font-size: 30px;\">
    +                                                     <i className=\"icon-spinner icon-spin\">
    +                                                         请等待, 计算任务依赖
    +                                                         <br />
    +                                                         <span style=\"font-size: 0.8em; color: #999\">
    +                                                             开始 {friendlyTimeDelta(jobStatus.baseStatus.jobStartTime)}
    +                                                         </span>
    +                                                     </i>
    +                                                 </h4>
    +                                                 <i className=\"icon-spinner icon-spin\" />
    +                                             </i>
    +                                         </i>
    +                                     </div>
    +                                 ) : null}
    +                                 <i className=\"icon-refresh\">
    +                                     <i className=\"icon-remove-sign\">
    +                                         <i className=\"icon-spinner icon-spin\">
    +                                             {/* WAITING CONFIRMATION / JOB PREVIEW */}
    +                                             {jobState == 'WAITING_CONFIRMATION' ? (
    +                                                 <div className=\"h100\">
    +                                                     <div
    +                                                         className=\"h100\"
    +                                                         include-no-scope=\"/templates/jobs/preview-status.html\"
    +                                                     />
    + 
    +                                                     {/* REGULAR */}
    +                                                     {jobState == 'RUNNING' ||
    +                                                     jobState == 'DONE' ||
    +                                                     jobState == 'FAILED' ||
    +                                                     jobState == 'ABORTED' ? (
    +                                                         <div className=\"h100\">
    +                                                             <div
    +                                                                 className=\"h100\"
    +                                                                 include-no-scope=\"/templates/jobs/regular-status.html\"
    +                                                             />
    +                                                         </div>
    +                                                     ) : null}
    +                                                 </div>
    +                                             ) : null}
    +                                         </i>
    +                                     </i>
    +                                 </i>
    +                             </div>
    +                         </div>
    +                     </i>
    +                 </i>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;