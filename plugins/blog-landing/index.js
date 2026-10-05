// Local Docusaurus plugin: the /blog landing page.
//
// The blog is split across several @docusaurus/plugin-content-blog
// instances (see blogSections in docusaurus.config.js). Nothing ships a
// page that spans instances, so this plugin collects the newest posts from
// each one in allContentLoaded - the only lifecycle hook that can see other
// plugins' content - and adds a single route rendering them.
//
// Options:
//   sections        [{ id, label, description }] - blog plugin instance ids,
//                   in display order (required)
//   routePath       route to add (default '/blog')
//   postsPerSection newest posts listed per section (default 5)
//   component       React component for the route (default
//                   @site/src/components/BlogLanding/index.jsx). It
//                   receives `sections`: [{ id, label, description,
//                   permalink, count, posts: [{ title, permalink, date,
//                   description, authors }] }]

const BLOG_PLUGIN = 'docusaurus-plugin-content-blog';

module.exports = function blogLandingPlugin(context, options) {
  const {
    sections = [],
    routePath = '/blog',
    postsPerSection = 5,
    component = '@site/src/components/BlogLanding/index.jsx',
  } = options || {};

  if (!Array.isArray(sections) || sections.length === 0) {
    throw new Error('[blog-landing] options.sections must list the blog plugin instances');
  }

  return {
    name: 'blog-landing',

    async allContentLoaded({ allContent, actions }) {
      const instances = (allContent && allContent[BLOG_PLUGIN]) || {};

      const data = sections.map(({ id, label, description }) => {
        const content = instances[id];
        if (!content || !Array.isArray(content.blogPosts)) {
          throw new Error(
            `[blog-landing] no ${BLOG_PLUGIN} instance with id "${id}" (configured ids: ${Object.keys(instances).join(', ') || 'none'})`,
          );
        }
        const listPage = Array.isArray(content.blogListPaginated) && content.blogListPaginated[0];
        const permalink = (listPage && listPage.metadata && listPage.metadata.permalink) || `/blog/${id}`;

        const posts = [...content.blogPosts]
          .sort((a, b) => new Date(b.metadata.date) - new Date(a.metadata.date))
          .slice(0, postsPerSection)
          .map(({ metadata }) => ({
            title: metadata.title,
            permalink: metadata.permalink,
            date: new Date(metadata.date).toISOString(),
            description: metadata.description || '',
            authors: (metadata.authors || []).map((a) => a.name).filter(Boolean),
          }));

        return {
          id,
          label: label || content.blogTitle || id,
          description: description || content.blogDescription || '',
          permalink,
          count: content.blogPosts.length,
          posts,
        };
      });

      const dataPath = await actions.createData('blog-landing.json', JSON.stringify(data));
      actions.addRoute({
        path: routePath,
        component,
        exact: true,
        modules: { sections: dataPath },
      });
    },
  };
};
