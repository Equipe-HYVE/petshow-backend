const core = require("@actions/core")
const github = require("@actions/github");

async function run() {
    try {
        const title = core.getInput("issue-title");
        const body = core.getInput("issue-body");
        const token = core.getInput("repo-token");

        const octokit = github.getOctokit(token);
        const {repo, owner} = github.context.repo;
        const newIssue = await octokit.issues.create({
            repo,
            owner,
            title,
            body
        })
    } catch(err) {
        core.setFailed(err.message);
    }
}

run();