import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"dss-page\">
    +             <tabs className=\"tabs-with-margin vertical-flex\">
    +                 <pane title=\"History\">
    +                     <div className=\"container-fluid page-top-padding fh h100 oa\">
    +                         <div block-api-error=\"\" />
    + 
    +                         {isProjectAdmin() && appConfig.gitMode == 'PROJECT' ? (
    +                             <div ng-controller=\"GitRemotesManagementController\">
    +                                 <div block-api-error=\"\">
    +                                     <div className=\"pull-right\">
    +                                         {remotes.map((remote, index: number) => {
    +                                             return (
    +                                                 <span key={`item-${index}`}>
    +                                                     <button
    +                                                         className=\"btn btn-primary\"
    +                                                         onClick={() => {
    +                                                             pushToRemote(remote.name);
    +                                                         }}>
    +                                                         推送到 {remote.name}
    +                                                     </button>
    +                                                 </span>
    +                                             );
    +                                         })}
    +                                         <button
    +                                             className=\"btn btn-default\"
    +                                             onClick={() => {
    +                                                 openModal();
    +                                             }}>
    +                                             管理远程
    +                                         </button>
    +                                     </div>
    +                                 </div>
    + 
    +                                 <div infinite-scroll=\"loadMore()\">
    +                                     <div style=\"width: 1000px; margin: auto\">
    +                                         <div
    +                                             git-log=\"\"
    +                                             log-entries=\"logEntries\"
    +                                             last-status=\"hasMore ? '正在加载下一个提交…' : '工程创建'\"
    +                                             className=\"h100\"
    +                                             project-revertable=\"true\"
    +                                             commit-revertable=\"true\"
    +                                         />
    +                                     </div>
    +                                 </div>
    + 
    +                                 {projectSummary.commitMode && projectSummary.commitMode != 'AUTO' ? (
    +                                     <pane title=\"提交\">
    +                                         <div className=\"container-fluid page-top-padding fh h100 oa\">
    +                                             <div block-api-error=\"\" />
    + 
    +                                             {!diffEntries.length ? <p className=\"centered-info\">暂无提交</p> : null}
    + 
    +                                             {diffEntries.length ? (
    +                                                 <div
    +                                                     style=\"width: 800px; margin: auto; \"
    +                                                     autocompletable-textarea=\"\"
    +                                                     no-markdown=\"true\">
    +                                                     <div
    +                                                         className=\"dku-border code-mirror dynamic-height\"
    +                                                         style=\"margin-bottom: 10px;\">
    +                                                         {editorOptions ? (
    +                                                             <textarea
    +                                                                 style=\"width: 100%; height: 100px; box-sizing: border-box;\"
    +                                                                 placeholder=\"Commit message\"
    +                                                                 value={commitMessage.message}
    +                                                                 ui-codemirror=\"editorOptions\"
    +                                                             />
    +                                                         ) : null}
    +                                                     </div>
    +                                                     <div style=\"text-align: right;\">
    +                                                         <button
    +                                                             className=\"btn btn-success\"
    +                                                             onClick={() => {
    +                                                                 commit();
    +                                                             }}
    +                                                             disabled-if-ro=\"\">
    +                                                             提交
    +                                                         </button>
    +                                                     </div>
    + 
    +                                                     <div style=\"margin-top: 50px;\">将提交以下更改:</div>
    + 
    +                                                     <div git-diff=\"\" diff-entries=\"diffEntries\" className=\"h100\" />
    +                                                 </div>
    +                                             ) : null}
    +                                         </div>
    +                                     </pane>
    +                                 ) : null}
    +                             </div>
    +                         ) : null}
    +                     </div>
    +                 </pane>
    +             </tabs>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;