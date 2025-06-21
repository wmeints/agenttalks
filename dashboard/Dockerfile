FROM node:22-alpine

WORKDIR /app
COPY build build/
COPY node_modules node_modules/
COPY package.json package.json

EXPOSE 3000

ENV NODE_ENV=production

CMD ["node", "build/index.js"]