import { MicVocal } from "lucide-react";

export default function Logo() {
  return (
    <div className="flex flex-row space-x-2">
      <MicVocal className="h-6 w-6" />
      <span className="text-lg font-bold">Agenttalks</span>
    </div>
  );
}
