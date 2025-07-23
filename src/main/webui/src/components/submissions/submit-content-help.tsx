import { Badge } from "../ui/badge";

export default function SubmitContentHelp() {
    return (
        <div
            className="mt-8 p-6 bg-blue-50 dark:bg-blue-900/20 border border-blue-200 dark:border-blue-800 rounded-lg shadow-sm"
        >
            <h3 className="text-lg font-semibold text-blue-900 dark:text-blue-100 mb-3">
                What happens next?
            </h3>
            <ul className="space-y-2 text-sm text-blue-800 dark:text-blue-200">
                <li className="flex items-start space-x-2">
                    <Badge variant="outline" className="mt-0.5 text-xs">1</Badge>
                    <span>We'll fetch and analyze the content from your URL</span>
                </li>
                <li className="flex items-start space-x-2">
                    <Badge variant="outline" className="mt-0.5 text-xs">2</Badge>
                    <span>We'll extract key information and metadata</span
                    >
                </li>
                <li className="flex items-start space-x-2">
                    <Badge variant="outline" className="mt-0.5 text-xs">3</Badge>
                    <span
                    >The content will be processed and included in the upcoming
                        podcast episode</span
                    >
                </li>
            </ul>
        </div>
    )
}